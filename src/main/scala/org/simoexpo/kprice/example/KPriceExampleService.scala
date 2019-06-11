package org.simoexpo.kprice.example

import org.http4s.implicits._
import org.http4s.server.Router
import org.simoexpo.kprice.example.routes.{HealthCheckRoutes, OrderRoutes, UsersRoutes}

object KPriceExampleService {

  val healthCheckService = new HealthCheckRoutes()
  val usersRoute = new UsersRoutes()
  val orderRoutes = new OrderRoutes()

  val httpApp = Router("/" -> healthCheckService.routes, "/" -> usersRoute.routes, "/" -> orderRoutes.routes).orNotFound

}
