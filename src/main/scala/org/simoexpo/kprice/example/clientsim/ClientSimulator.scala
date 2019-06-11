package org.simoexpo.kprice.example.clientsim

import java.util.UUID

import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import org.http4s._
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.simoexpo.kprice.example.clientsim.protocol.{JsonDecoders, JsonEncoders}
import org.simoexpo.kprice.example.models.ProductItem.ProductItem
import org.simoexpo.kprice.example.models.{ProductItem, UserStatus}
import org.simoexpo.kprice.example.routes.models.{OrderRequestContract, UserSignUpContract, UserVerificationContract, UserView}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random

object ClientSimulator extends IOApp with JsonDecoders with JsonEncoders {

  val MAX_CUSTOMER: Long = 10000L

  val draftClients: Ref[IO, List[UserView]] = Ref.unsafe(List.empty[UserView])
  val activeClient: Ref[IO, List[UserView]] = Ref.unsafe(List.empty[UserView])
  val products: List[ProductItem] = ProductItem.values.toList

  val createUser: Client[IO] => IO[Unit] = client => {
    for {
      activeClient <- activeClient.get
      _ <- if (activeClient.length < MAX_CUSTOMER) {
        val name = UUID.randomUUID().toString
        val body = UserSignUpContract(name, s"$name@mail.com")
        val req = Request[IO](method = Method.POST, uri = uri"http://127.0.0.1:8080/users").withEntity(body)
        for {
          userView <- client.expect[UserView](req)
          _ <- draftClients.update(s => userView +: s)
        } yield ()
      } else {
        IO.unit
      }
    } yield ()
  }

  val validateUser: Client[IO] => IO[Unit] = client =>
    for {
      maybeUser <- draftClients.modify {
        case Nil => (Nil, None)
        case s   => (s.tail, s.headOption)
      }
      _ <- maybeUser match {
        case None => IO.unit
        case Some(user) =>
          val body = UserVerificationContract(UserStatus.Valid)
          val url = s"http://127.0.0.1:8080/users/${user.id}/verify"
          val req = Request[IO](method = Method.POST, uri = Uri.unsafeFromString(url)).withEntity(body)
          for {
            _ <- client.expect[Unit](req)
            _ <- activeClient.update(s => user +: s)
          } yield ()
      }
    } yield ()

  val createOrder: Client[IO] => IO[Unit] = client =>
    for {
      activeUsers <- activeClient.get
      _ <- activeUsers match {
        case Nil => IO.unit
        case _ =>
          val user = activeUsers(Random.nextInt(activeUsers.length))
          val order = OrderRequestContract(user.id, products(Random.nextInt(products.length)), Random.nextInt(10) + 1)
          val req = Request[IO](method = Method.POST, uri = uri"http://127.0.0.1:8080/orders").withEntity(order)
          client.expect[Unit](req)
      }
    } yield ()

  val ops = List(createUser, validateUser, createOrder)

  val doSomething: Client[IO] => IO[Unit] = client => {
    for {
      n <- IO(Random.nextInt(ops.length))
      _ <- ops(n)(client)
      _ <- IO.sleep(50 millis)
    } yield ()
  }.foreverM

  override def run(args: List[String]): IO[ExitCode] =
    BlazeClientBuilder[IO](global).resource.use(doSomething).map(_ => ExitCode.Success)
}
