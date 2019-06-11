package org.simoexpo.kprice.example.routes.models

import org.simoexpo.kprice.example.models.OrderRequest
import org.simoexpo.kprice.example.models.ProductItem.ProductItem
import org.simoexpo.kprice.example.routes.models

case class OrderRequestView(id: Long, userId: Long, product: ProductItem, quantity: Int)

object OrderRequestView {

  def from(orderRequest: OrderRequest): OrderRequestView =
    models.OrderRequestView(orderRequest.id, orderRequest.userId, orderRequest.product, orderRequest.quantity)

}
