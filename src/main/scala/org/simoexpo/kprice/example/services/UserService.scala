package org.simoexpo.kprice.example.services

import cats.effect.IO
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.streams.scala.Serdes._
import org.simoexpo.kprice.example.kafka.SchemaRegistrySerdes._
import org.simoexpo.kprice.example.kafka.Topics
import org.simoexpo.kprice.example.kafka.models.{User => UserAvro, UserStatus => UserStatusAvro}
import org.simoexpo.kprice.example.kafka.utils.{KafkaProducerConf, KafkaProducerFactory}
import org.simoexpo.kprice.example.models.User

object UserService {

  implicit val userProducerConf: KafkaProducerConf[String, UserAvro] = KafkaProducerConf("users-producer")
  val usersProducer: KafkaProducer[String, UserAvro] = KafkaProducerFactory[String, UserAvro]

  def signUpUser(user: User) = IO {
    val userAvro = UserAvro(user.id, user.userName, user.email, UserStatusAvro.valueOf(user.status.toString))
    usersProducer.send(new ProducerRecord(Topics.Users, user.id.toString, userAvro)).get()
    user
  }
}
