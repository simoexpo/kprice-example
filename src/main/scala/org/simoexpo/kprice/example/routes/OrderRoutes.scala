package org.simoexpo.kprice.example.routes

import cats.effect.IO
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._
import org.simoexpo.kprice.example.mapper.OrderRequestMapper
import org.simoexpo.kprice.example.routes.models.{OrderRequestContract, OrderRequestView, OrderView}
import org.simoexpo.kprice.example.routes.protocol.{JsonDecoders, JsonEncoders}
import org.simoexpo.kprice.example.services.OrderService

class OrderRoutes extends JsonDecoders with JsonEncoders {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "orders" =>
      for {
        orderContract <- req.as[OrderRequestContract]
        order <- OrderService.createOrder(OrderRequestMapper.from(orderContract))
        resp <- Created(OrderRequestView.from(order).asJson)
      } yield resp

    case GET -> Root / "orders" / "count" =>
      for {
        productOrderCount <- OrderService.getOrdersCount
        resp <- Ok(productOrderCount.asJson)
      } yield resp

    case GET -> Root / "orders" / id =>
      for {
        maybeOrder <- OrderService.get(id)
        resp <- maybeOrder match {
          case Some(order) => Ok(OrderView.from(order).asJson)
          case None        => NotFound()
        }
      } yield resp

  }
}
