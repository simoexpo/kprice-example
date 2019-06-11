package org.simoexpo.kprice.example.kafka.streams

import java.util.Properties

import cats.effect.IO
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.streams.kstream.GlobalKTable
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.simoexpo.kprice.example.kafka.SchemaRegistrySerdes._
import org.simoexpo.kprice.example.kafka.models._
import org.simoexpo.kprice.example.kafka.Topics

object UsersStreams {

//  private val logger = Logger("stream-logger")

  private val settings = new Properties()
  settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-lens-users")
  settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
  settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, classOf[SpecificAvroSerde[_]])
  settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[SpecificAvroSerde[_]])
  settings.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://127.0.0.1:8081")

  private val builder = new StreamsBuilder

  val s = new SpecificAvroSerde()

  private val users: GlobalKTable[String, User] = builder.globalTable(Topics.Users)

  builder
    .stream[String, Verification](Topics.UsersVerification)
    .join(users)((_, verification) => verification.userId.toString,
                 (verification, userName) => userName.copy(status = verification.userStatus))
    .to(Topics.UsersWithVerification)

  private val userWithVerification: KStream[String, User] =
    builder.stream(Topics.UsersWithVerification)

  userWithVerification.filter((_, user) => user.status == UserStatus.Valid).to(Topics.ValidUsers)

  userWithVerification.filter((_, user) => user.status == UserStatus.Invalid).to(Topics.InvalidUsers)

  private val topology = builder.build

  val streams = new KafkaStreams(topology, settings)

  def startStream = IO {

    streams.start()

    sys.addShutdownHook {
      System.out.println("### Stopping Application ###")
      streams.close()
    }
  }
}
