package org.simoexpo.kprice.example.mapper

import org.simoexpo.kprice.example.models.OrderRequest
import org.simoexpo.kprice.example.routes.models.OrderRequestContract

import scala.util.Random

object OrderRequestMapper {

  def from(orderRequestContract: OrderRequestContract): OrderRequest =
    OrderRequest(Random.nextLong.abs, orderRequestContract.userId, orderRequestContract.product, orderRequestContract.quantity)

}
