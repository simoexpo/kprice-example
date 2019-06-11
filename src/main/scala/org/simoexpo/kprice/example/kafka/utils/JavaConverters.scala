package org.simoexpo.kprice.example.kafka.utils

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit._

import scala.concurrent.duration.FiniteDuration
import scala.language.implicitConversions

object JavaConverters {

  implicit def toJavaDuration(finiteDuration: FiniteDuration): Duration =
    finiteDuration.unit match {
      case DAYS         => Duration.of(finiteDuration.toDays, ChronoUnit.DAYS)
      case HOURS        => Duration.of(finiteDuration.toHours, ChronoUnit.HOURS)
      case MICROSECONDS => Duration.of(finiteDuration.toMicros, ChronoUnit.MICROS)
      case MILLISECONDS => Duration.of(finiteDuration.toMillis, ChronoUnit.MILLIS)
      case MINUTES      => Duration.of(finiteDuration.toMinutes, ChronoUnit.MINUTES)
      case NANOSECONDS  => Duration.of(finiteDuration.toNanos, ChronoUnit.NANOS)
      case SECONDS      => Duration.of(finiteDuration.toSeconds, ChronoUnit.SECONDS)
    }

}
