package org.simoexpo.kprice.example.kafka

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.avro.specific.SpecificRecord
import org.simoexpo.kprice.example.kafka.models._

import scala.collection.JavaConverters._

object SchemaRegistrySerdes {

  private val schemaRegistryUrl: String = "http://127.0.0.1:8081"

  implicit val userSerde: SpecificAvroSerde[User] = valueSerde[User]
  implicit val verificationSerde: SpecificAvroSerde[Verification] = valueSerde[Verification]
  implicit val orderSerde: SpecificAvroSerde[Order] = valueSerde[Order]
  implicit val orderRequestSerde: SpecificAvroSerde[OrderRequest] = valueSerde[OrderRequest]

  private def keySerde[T <: SpecificRecord]: SpecificAvroSerde[T] = {
    val serde = new SpecificAvroSerde[T]
    serde.configure(Map(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> schemaRegistryUrl).asJava, true)
    serde
  }

  private def valueSerde[T <: SpecificRecord]: SpecificAvroSerde[T] = {
    val serde = new SpecificAvroSerde[T]
    serde.configure(Map(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> schemaRegistryUrl).asJava, false)
    serde
  }
}
