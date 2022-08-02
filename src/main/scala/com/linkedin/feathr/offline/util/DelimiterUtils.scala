package com.linkedin.feathr.offline.util

object DelimiterUtils {

  /**
   * Convert delimiter to an escape character (e.g. "   " -> "\t")
   */
  def escape(raw: String): String = {
    import scala.reflect.runtime.universe.{Literal, Constant}
    Literal(Constant(raw)).toString.replaceAll("\"", "")
  }

}
