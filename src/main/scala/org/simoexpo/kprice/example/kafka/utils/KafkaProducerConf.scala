package org.simoexpo.kprice.example.kafka.utils

import org.apache.kafka.common.serialization.{Serde, Serializer}

case class KafkaProducerConf[K, V](settings: Map[String, String], keySerializer: Serializer[K], valueSerializer: Serializer[V])

object KafkaProducerConf {

//  def apply[K, V](config: String)(implicit keySerializer: Serializer[K],
//                                  valueSerializer: Serializer[V]): KafkaProducerConf[K, V] =
//    pureconfig.loadConfig[ProducerConf](config) match {
//      case Right(conf) =>
//        val producerSetting = conf.settings.map {
//          case (key, value) => (key.replaceAll("-", "."), value)
//        }
//        new KafkaProducerConf(producerSetting, keySerializer, valueSerializer)
//      case Left(ex) => throw new IllegalArgumentException(s"Cannot load producer setting from $config: $ex")
//    }

  import pureconfig.generic.auto._

  def apply[K, V](config: String)(implicit keySerde: Serde[K], valueSerde: Serde[V]): KafkaProducerConf[K, V] =
    pureconfig.loadConfig[ProducerConf](config) match {
      case Right(conf) =>
        val producerSetting = conf.settings.map {
          case (key, value) => (key.replaceAll("-", "."), value)
        }
        new KafkaProducerConf(producerSetting, keySerde.serializer(), valueSerde.serializer())
      case Left(ex) => throw new IllegalArgumentException(s"Cannot load producer setting from $config: $ex")
    }

  private final case class ProducerConf(settings: Map[String, String])

}
