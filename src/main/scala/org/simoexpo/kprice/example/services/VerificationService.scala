package org.simoexpo.kprice.example.services

import cats.effect.IO
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.streams.scala.Serdes._
import org.simoexpo.kprice.example.kafka.SchemaRegistrySerdes._
import org.simoexpo.kprice.example.kafka.Topics
import org.simoexpo.kprice.example.kafka.models.{UserStatus => UserStatusAvro, Verification => VerificationAvro}
import org.simoexpo.kprice.example.kafka.utils.{KafkaProducerConf, KafkaProducerFactory}
import org.simoexpo.kprice.example.models.Verification

object VerificationService {

  implicit val usersVerificationProducerConf: KafkaProducerConf[String, VerificationAvro] =
    KafkaProducerConf("users-verification-producer")
  val usersVerificationProducer: KafkaProducer[String, VerificationAvro] = KafkaProducerFactory[String, VerificationAvro]

  def updateUserVerification(verification: Verification): IO[Unit] = IO {
    val verificationAvro = VerificationAvro(verification.userId, UserStatusAvro.valueOf(verification.userStatus.toString))
    usersVerificationProducer
      .send(new ProducerRecord(Topics.UsersVerification, verification.userId.toString, verificationAvro))
      .get()
    ()
  }

}
