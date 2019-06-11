package org.simoexpo.kprice.example.models

import ProductItem.ProductItem

case class Order(id: Long, userId: Long, email: String, product: ProductItem, quantity: Int)

object Order {

  def from(user: User, order: OrderRequest): Order =
    Order(order.id, order.userId, user.email, order.product, order.quantity)

}
