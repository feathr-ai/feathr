package com.linkedin.feathr.offline.config.anchors

import com.linkedin.feathr.exception.{ErrorLabel, FrameConfigException}

import java.time.Duration
import com.linkedin.feathr.featureDataModel.{Unit, Window}

/**
 * Implementation should define how [[Window]] is converted to [[Duration]]
 */
private[feathr] trait PegasusRecordDurationConverter {
  def convert(frWindow: Window): Duration
}

/**
 * The converter to convert FeatureRegistry Pegasus Window record to [[Duration]]
 * This can be shared between FeatureDef and Join config
 */
private[feathr] object PegasusRecordDurationConverter extends PegasusRecordDurationConverter {
  def convert(frWindow: Window): Duration = {
    try {
      val size = frWindow.getSize.intValue()
      frWindow.getUnit match {
        case Unit.DAY => Duration.ofDays(size)
        case Unit.HOUR => Duration.ofHours(size)
        case Unit.MINUTE => Duration.ofMinutes(size)
        case Unit.SECOND => Duration.ofSeconds(size)
      }
    } catch {
      case _: Exception =>
        throw new FrameConfigException(
          ErrorLabel.FRAME_USER_ERROR,
          s"'window' field($frWindow) is not correctly set. The correct example " +
            "can be '1d'(1 day) or '2h'(2 hour) or '3m'(3 minute) or '4s'(4 second) ")
    }
  }
}
