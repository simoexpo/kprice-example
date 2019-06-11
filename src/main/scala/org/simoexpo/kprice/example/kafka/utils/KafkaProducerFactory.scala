package org.simoexpo.kprice.example.kafka.utils

import org.apache.kafka.clients.producer.KafkaProducer

import scala.collection.JavaConverters._

object KafkaProducerFactory {

  def apply[K, V](implicit kafkaProducerConf: KafkaProducerConf[K, V]): KafkaProducer[K, V] =
    new KafkaProducer[K, V](kafkaProducerConf.settings.asInstanceOf[Map[String, Object]].asJava,
                            kafkaProducerConf.keySerializer,
                            kafkaProducerConf.valueSerializer)

}
