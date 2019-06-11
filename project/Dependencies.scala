import sbt._

object Dependencies {

  object CustomResolvers {
    lazy val Confluent = "confluent" at "https://packages.confluent.io/maven/"
  }

  private lazy val KafkaVersion = "2.2.1"
  private lazy val Http4sVersion = "0.20.1"
  private lazy val CirceVersion = "0.11.1"
  private lazy val PureConfigVersion = "0.11.0"
  private lazy val ScalaLoggingVersion = "3.9.2"
  private lazy val AvroSerializerVersion = "5.2.1"
  private lazy val LogbackClassicVersion = "1.2.3"

  lazy val kafka = "org.apache.kafka" % "kafka-clients" % KafkaVersion
  lazy val kafkaStreams = "org.apache.kafka" %% "kafka-streams-scala" % KafkaVersion
  lazy val http4s = Seq(
    "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
    "org.http4s" %% "http4s-dsl" % Http4sVersion,
    "org.http4s" %% "http4s-circe" % Http4sVersion
  )
  lazy val circe = "io.circe" %% "circe-generic" % CirceVersion
  lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % PureConfigVersion
  lazy val logback = "ch.qos.logback" % "logback-classic" % LogbackClassicVersion
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion
  lazy val avroSerde = "io.confluent" % "kafka-streams-avro-serde" % AvroSerializerVersion

}
