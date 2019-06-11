package org.simoexpo.kprice.example.clientsim.protocol

import cats.effect.IO
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe.jsonOf
import org.simoexpo.kprice.example.models.UserStatus
import org.simoexpo.kprice.example.routes.models.UserView

trait JsonDecoders {

  implicit val userStatusDecoder = Decoder.enumDecoder(UserStatus)
  implicit val userViewDecoder = deriveDecoder[UserView]
  implicit val userViewEntityDecoder = jsonOf[IO, UserView]

}
