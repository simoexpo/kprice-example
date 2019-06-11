package org.simoexpo.kprice.example.kafka.processor

import org.simoexpo.kprice.example.kafka.models.Order

class OrderStoreTransformer extends ValueStoreTransformer[String, Order] {

  override protected val storeName: String = "orders-store"

  override def transform(value: Order): Order = {
    kvStore.put(value.id.toString, value)
    value
  }
}
