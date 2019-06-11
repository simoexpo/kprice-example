package org.simoexpo.kprice.example.routes

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class HealthCheckRoutes {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "health" => Ok("feeling good!")
  }
}
