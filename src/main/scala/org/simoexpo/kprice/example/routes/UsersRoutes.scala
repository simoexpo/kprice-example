package org.simoexpo.kprice.example.routes

import cats.effect.IO
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._
import org.simoexpo.kprice.example.mapper.UserMapper
import org.simoexpo.kprice.example.models.Verification
import org.simoexpo.kprice.example.routes.models.{UserSignUpContract, UserVerificationContract, UserView}
import org.simoexpo.kprice.example.routes.protocol.{JsonDecoders, JsonEncoders}
import org.simoexpo.kprice.example.services.{UserService, VerificationService}

class UsersRoutes extends JsonDecoders with JsonEncoders {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "users" =>
      for {
        userContract <- req.as[UserSignUpContract]
        user <- UserService.signUpUser(UserMapper.from(userContract))
        resp <- Created(UserView.from(user).asJson)
      } yield resp

    case req @ POST -> Root / "users" / id / "verify" =>
      for {
        userVerificationContract <- req.as[UserVerificationContract]
        _ <- VerificationService.updateUserVerification(Verification(id.toLong, userVerificationContract.status))
        resp <- NoContent()
      } yield resp
  }
}
