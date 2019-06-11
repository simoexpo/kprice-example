package org.simoexpo.kprice.example.clientsim.protocol

import cats.effect.IO
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.circe.jsonEncoderOf
import org.simoexpo.kprice.example.models.{ProductItem, UserStatus}
import org.simoexpo.kprice.example.routes.models.{OrderRequestContract, UserSignUpContract, UserVerificationContract}

trait JsonEncoders {

  implicit val userStatusEncoder = Encoder.enumEncoder(UserStatus)

  implicit val productItemEncoder = Encoder.enumEncoder(ProductItem)
  implicit val orderRequestContractEncoder = deriveEncoder[OrderRequestContract]
  implicit val orderRequestContractEntityEncoder = jsonEncoderOf[IO, OrderRequestContract]

  implicit val userVerificationContractEncoder = deriveEncoder[UserVerificationContract]
  implicit val userVerificationContractEntityEncoder = jsonEncoderOf[IO, UserVerificationContract]

  implicit val UserSignUpContractEncoder = deriveEncoder[UserSignUpContract]
  implicit val UserSignUpContractEntityEncoder = jsonEncoderOf[IO, UserSignUpContract]

}
