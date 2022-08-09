package com.linkedin.feathr.offline.client.plugins

import scala.collection.mutable

/**
 * A shared registry for loading [[UdfAdaptor]]s, which basically can tell Feathr's runtime how to support different
 * kinds of "external" UDFs not natively known to Feathr, but which have similar behavior to Feathr's.
 *
 * All "external" UDF classes are required to have a public default zero-arg constructor.
 */
object FeathrUdfPluginContext {
  val registeredUdfAdaptors = mutable.Buffer[UdfAdaptor[_]]()

  def registerUdfAdaptor(adaptor: UdfAdaptor[_]): Unit = {
    this.synchronized {
      registeredUdfAdaptors += adaptor
    }
  }

  def getRegisteredUdfAdaptor(clazz: Class[_]): Option[UdfAdaptor[_]] = {
    registeredUdfAdaptors.find(_.canAdapt(clazz))
  }
}