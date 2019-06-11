package org.simoexpo.kprice.example.routes.protocol

import cats.effect.IO
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe.jsonOf
import org.simoexpo.kprice.example.models.{ProductItem, UserStatus}
import org.simoexpo.kprice.example.routes.models.{OrderRequestContract, UserSignUpContract, UserVerificationContract}

trait JsonDecoders {

  implicit val userSignUpContractDecoder = deriveDecoder[UserSignUpContract]
  implicit val userSignUpContractEntityDecoder = jsonOf[IO, UserSignUpContract]

  implicit val userStatusDecoder = Decoder.enumDecoder(UserStatus)
  implicit val userVerificationContractDecoder = deriveDecoder[UserVerificationContract]
  implicit val userVerificationContractEntityDecoder = jsonOf[IO, UserVerificationContract]

  implicit val productItemDecoder = Decoder.enumDecoder(ProductItem)
  implicit val orderRequestContractDecoder = deriveDecoder[OrderRequestContract]
  implicit val orderRequestContractEntityDecoder = jsonOf[IO, OrderRequestContract]

}
