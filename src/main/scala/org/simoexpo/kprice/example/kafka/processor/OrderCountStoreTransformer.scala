package org.simoexpo.kprice.example.kafka.processor

import org.apache.kafka.streams.KeyValue

class OrderCountStoreTransformer extends StoreTransformer[String, Long] {

  override protected def storeName: String = "orders-count-store"

  override def transform(key: String, value: Long): KeyValue[String, Long] = {
    kvStore.put(key, value)
    new KeyValue(key, value)
  }
}
