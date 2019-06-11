package org.simoexpo.kprice.example.kafka.processor

import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.{Transformer, ValueTransformer}
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.state.KeyValueStore

trait ValueStoreTransformer[K, V] extends ValueTransformer[V, V] {

  protected var context: ProcessorContext = _
  protected var kvStore: KeyValueStore[K, V] = _

  protected def storeName: String

  override def init(context: ProcessorContext): Unit = {
    this.context = context
    kvStore = context.getStateStore(storeName).asInstanceOf[KeyValueStore[K, V]]
  }

  override def close(): Unit = ()

}

trait StoreTransformer[K, V] extends Transformer[K, V, KeyValue[K, V]] {

  protected var context: ProcessorContext = _
  protected var kvStore: KeyValueStore[K, V] = _

  protected def storeName: String

  override def init(context: ProcessorContext): Unit = {
    this.context = context
    kvStore = context.getStateStore(storeName).asInstanceOf[KeyValueStore[K, V]]
  }

  override def close(): Unit = ()

}
