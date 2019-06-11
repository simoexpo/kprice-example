package org.simoexpo.kprice.example.kafka.streams

import java.util.Properties

import cats.effect.IO
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.kstream.Suppressed.BufferConfig
import org.apache.kafka.streams.kstream.{SessionWindows, Suppressed, TimeWindows}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.state._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.simoexpo.kprice.example.kafka.Topics
import org.simoexpo.kprice.example.kafka.processor.{OrderCountStoreTransformer, OrderStoreTransformer}
import org.simoexpo.kprice.example.kafka.utils.JavaConverters._
import org.simoexpo.kprice.example.kafka.utils.KTableSuppressSupport._
//import org.simoexpo.kprice.example.models.{Order, OrderRequest, User}
import org.simoexpo.kprice.example.kafka.SchemaRegistrySerdes._
import org.simoexpo.kprice.example.kafka.models._

import scala.concurrent.duration._
import scala.language.postfixOps

object OrdersStream {

  object StateStores {
    lazy val orderStore: ReadOnlyKeyValueStore[String, Order] =
      OrdersStream.streams.store("orders-store", QueryableStoreTypes.keyValueStore[String, Order]())

    lazy val orderCountStore: ReadOnlyKeyValueStore[String, Long] =
      OrdersStream.streams.store("orders-count-store", QueryableStoreTypes.keyValueStore[String, Long]())
  }

  private val settings = new Properties()
  settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-lens-orders")
  settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
  settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, classOf[SpecificAvroSerde[_]])
  settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[SpecificAvroSerde[_]])
  settings.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://127.0.0.1:8081")
  // to speed up aggregation result otherwise simply decrease COMMIT_INTERVAL or CACHE_MAX_BYTES_BUFFERING_CONFIG
  //  settings.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE)
  settings.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "10000")
  //  settings.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "1024")

  private val ordersStoreBuilder: StoreBuilder[KeyValueStore[String, Order]] =
    Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore("orders-store"),
                                implicitly[Serde[String]],
                                implicitly[Serde[Order]])

  private val ordersCountStoreBuilder: StoreBuilder[KeyValueStore[String, Long]] =
    Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore("orders-count-store"),
                                implicitly[Serde[String]],
                                implicitly[Serde[Long]])

  private val builder = new StreamsBuilder

  builder.addStateStore(ordersStoreBuilder)
  builder.addStateStore(ordersCountStoreBuilder)

  private val validUser: KTable[String, User] =
    builder.table(Topics.ValidUsers)

  private val orders: KStream[String, OrderRequest] = builder.stream(Topics.OrderRequests)

  private val orderPerUser = orders
    .join(validUser)((order, user) => Order(order.id, user.id, user.email, order.product, order.quantity))
    .transformValues[Order](() => new OrderStoreTransformer, "orders-store")

  orderPerUser.to(Topics.OrdersToProcess)

  orderPerUser.groupByKey
    .windowedBy(SessionWindows.`with`(10 minutes))
    .aggregate(0L)((_, order, acc) => acc + order.quantity, (_, v1, v2) => v1 + v2)
    .toStream
    // on table I also get 0 (0 is null?)
    .filter((_, v) => v > 100L)
    .peek((k, v) => println(s"Deluxe User ${k.key()} did $v order from ${k.window().startTime()} to ${k.window().endTime()}"))

  private val orderToProcess: KStream[String, Order] =
    builder.stream(Topics.OrdersToProcess)

  orderToProcess
    .groupBy((_, v) => v.product.toString)
    .windowedBy(TimeWindows.of(10 seconds).grace(0 second))
    .aggregate(0L)((_, order, acc) => acc + order.quantity)
    .suppress(Suppressed.untilWindowCloses(BufferConfig.unbounded()))
    .toStream
    .peek((k, v) => println(s"[${k.window().startTime()} - ${k.window().endTime()}] ${k.key()}: $v"))
    .map((k, v) => (k.key(), v))
    .to(Topics.OrderCount)

  private val orderCount: KStream[String, Long] =
    builder.stream(Topics.OrderCount)

  orderCount.groupByKey
    .aggregate(0L)((_, quantity, acc) => acc + quantity)
    .toStream
    .transform[String, Long](() => new OrderCountStoreTransformer, "orders-count-store")

  orderCount.filter((_, count) => count > 100).peek((k, v) => println(s"WARING: high orders ($v) for $k"))

  private val topology = builder.build

  private val streams = new KafkaStreams(topology, settings)

  def startStream = IO {

    streams.start()

    sys.addShutdownHook {
      System.out.println("### Stopping Application ###")
      streams.close()
    }
  }
}
