package org.simoexpo.kprice.example.routes.protocol

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Encoder, KeyEncoder}
import org.simoexpo.kprice.example.models.ProductItem.ProductItem
import org.simoexpo.kprice.example.models.{ProductItem, UserStatus}
import org.simoexpo.kprice.example.routes.models.{OrderRequestView, OrderView, UserView}

trait JsonEncoders {

  implicit val userStatusEncoder = Encoder.enumEncoder(UserStatus)
  implicit val userViewEncoder = deriveEncoder[UserView]
  implicit val productItemEncoder = Encoder.enumEncoder(ProductItem)
  implicit val orderRequestViewEncoder = deriveEncoder[OrderRequestView]
  implicit val orderViewEncoder = deriveEncoder[OrderView]

  implicit val productItemKeyEncoder = new KeyEncoder[ProductItem] {
    override def apply(productItem: ProductItem): String = productItem.toString
  }

}
