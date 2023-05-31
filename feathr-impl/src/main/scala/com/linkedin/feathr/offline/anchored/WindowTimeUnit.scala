package com.linkedin.feathr.offline.anchored

import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrConfigException}

import java.time.Duration



/**
 * WindowTimeUnit is to describe time unit setting for time-window feature
 * 'D' means day ; 'H' means hour ; 'M' means minute ; 'S' means second
 */
private[offline] object WindowTimeUnit extends Enumeration {
  type WindowTimeUnit = Value
  val D, H, M, S, W, Y = Value

  def parseWindowTime(timeWindowStr: String): Duration = {
    try {
      val timeUnit = WindowTimeUnit.withName(timeWindowStr takeRight 1 toUpperCase)
      timeUnit match {
        case D => Duration.ofDays(timeWindowStr.dropRight(1).trim.toLong)
        case H => Duration.ofHours(timeWindowStr.dropRight(1).trim.toLong)
        case M => Duration.ofMinutes(timeWindowStr.dropRight(1).trim.toLong)
        case S => Duration.ofSeconds(timeWindowStr.dropRight(1).trim.toLong)
        case Y => Duration.ofDays(365*timeWindowStr.dropRight(1).trim.toLong)
        case W => Duration.ofDays(7*timeWindowStr.dropRight(1).trim.toLong)
        case _ => Duration.ofSeconds(0)
      }
    } catch {
      case ex: Exception =>
        throw new FeathrConfigException(
          ErrorLabel.FEATHR_USER_ERROR,
          s"'window' field($timeWindowStr) is not correctly set. The correct example " +
            "can be '1d'(1 day) or '2h'(2 hour)")
    }
  }
}
