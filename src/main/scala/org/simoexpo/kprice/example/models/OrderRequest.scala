package org.simoexpo.kprice.example.models

import ProductItem.ProductItem

case class OrderRequest(id: Long, userId: Long, product: ProductItem, quantity: Int)
