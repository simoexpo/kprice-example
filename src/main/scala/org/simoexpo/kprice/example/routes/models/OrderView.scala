package org.simoexpo.kprice.example.routes.models

import org.simoexpo.kprice.example.models.ProductItem.ProductItem
import org.simoexpo.kprice.example.models.Order

case class OrderView(id: Long, userId: Long, email: String, product: ProductItem, quantity: Int)

object OrderView {

  def from(order: Order) = OrderView(order.id, order.userId, order.email, order.product, order.quantity)

}
