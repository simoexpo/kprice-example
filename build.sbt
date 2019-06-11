import Dependencies._

resolvers += CustomResolvers.Confluent

organization := "org.simoexpo"

name := "KPrice Example"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(kafka, kafkaStreams, circe, pureConfig, logback, scalaLogging, avroSerde) ++ http4s

scalacOptions in Compile := Seq("-deprecation")

lazy val avroHuggerSettings = Seq(
  sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue
)

lazy val kPriceExample =
  (project in file(".")).settings(avroHuggerSettings)
