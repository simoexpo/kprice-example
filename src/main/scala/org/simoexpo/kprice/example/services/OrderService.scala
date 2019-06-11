package org.simoexpo.kprice.example.services

import cats.effect.IO
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.simoexpo.kprice.example.kafka.SchemaRegistrySerdes._
import org.simoexpo.kprice.example.kafka.Topics
import org.simoexpo.kprice.example.kafka.models.{
  Order => OrderAvro,
  OrderRequest => OrderRequestAvro,
  ProductItem => ProductItemAvro
}
import org.simoexpo.kprice.example.kafka.streams.OrdersStream
import org.simoexpo.kprice.example.kafka.utils.{KafkaProducerConf, KafkaProducerFactory}
import org.simoexpo.kprice.example.models.ProductItem.ProductItem
import org.simoexpo.kprice.example.models.{Order, OrderRequest, ProductItem}

object OrderService {

  implicit val ordersProducerConf: KafkaProducerConf[String, OrderRequestAvro] = KafkaProducerConf("order-requests-producer")
  val ordersProducer: KafkaProducer[String, OrderRequestAvro] = KafkaProducerFactory[String, OrderRequestAvro]

  def createOrder(order: OrderRequest) = IO {
    val orderRequestAvro =
      OrderRequestAvro(order.id, order.userId, ProductItemAvro.valueOf(order.product.toString), order.quantity)
    ordersProducer.send(new ProducerRecord(Topics.OrderRequests, order.userId.toString, orderRequestAvro)).get()
    order
  }

  def get(id: String): IO[Option[Order]] = IO {
    val orderStore: ReadOnlyKeyValueStore[String, OrderAvro] = OrdersStream.StateStores.orderStore
    Option(orderStore.get(id)).map(order =>
      Order(order.id, order.userId, order.email, ProductItem.withName(order.product.name()), order.quantity))
  }

  def getOrdersCount: IO[Map[ProductItem, Long]] = IO {

    val ordersCountStore: ReadOnlyKeyValueStore[String, Long] = OrdersStream.StateStores.orderCountStore
    val productOrderCount = for {
      product <- ProductItem.values.toList
      productCount = Option(ordersCountStore.get(product.toString)).getOrElse(0L)
    } yield product -> productCount
    productOrderCount.toMap
  }

}
