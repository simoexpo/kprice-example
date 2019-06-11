package org.simoexpo.kprice.example.routes.models

import org.simoexpo.kprice.example.models.ProductItem.ProductItem

case class OrderRequestContract(userId: Long, product: ProductItem, quantity: Int)
