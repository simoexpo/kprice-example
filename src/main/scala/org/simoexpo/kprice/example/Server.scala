package org.simoexpo.kprice.example

import cats.effect._
import cats.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.simoexpo.kprice.example.kafka.streams.{OrdersStream, UsersStreams}
import org.simoexpo.kprice.example.kafka.streams.{OrdersStream, UsersStreams}

object Server extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- UsersStreams.startStream
      _ <- OrdersStream.startStream
      http <- BlazeServerBuilder[IO].bindHttp().withHttpApp(KPriceExampleService.httpApp).serve.compile.drain.as(ExitCode.Success)
    } yield http

}
