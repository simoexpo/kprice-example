package org.simoexpo.kprice.example.kafka.utils

import org.apache.kafka.streams.kstream.Suppressed
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.scala.ImplicitConversions._

object KTableSuppressSupport {

  implicit class KTableWithSuppress[K, V](kTable: KTable[K, V]) {

    def suppress(suppressed: Suppressed[_ >: K]): KTable[K, V] =
      kTable.inner.suppress(suppressed)
  }

}
