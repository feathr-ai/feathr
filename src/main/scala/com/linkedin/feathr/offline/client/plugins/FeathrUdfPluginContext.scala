package com.linkedin.feathr.offline.client.plugins

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters._

object FeathrUdfPluginContext {
  val registeredUdfAdaptors = new ConcurrentHashMap[Class[_], UdfAdaptor]().asScala
  val adaptorSubclassCache = new ConcurrentHashMap[Class[_], Option[UdfAdaptor]]().asScala

  def registerUdf(className: Class[_], adaptor: UdfAdaptor): Unit = {
    this.synchronized {
      if (registeredUdfAdaptors.contains(className)) {
        throw new IllegalArgumentException(s"Class ${className} already registered.")
      }
      registeredUdfAdaptors.keys.foreach(alreadyRegistered => {
        if (alreadyRegistered.isAssignableFrom(className) || className.isAssignableFrom(alreadyRegistered)) {
          throw new IllegalArgumentException(s"Class to be registered ${className} is a subtype or supertype of already" +
            s" registered class ${alreadyRegistered}")
        }
      })
      registeredUdfAdaptors.put(className, adaptor)
      adaptorSubclassCache.clear()
    }
  }

  def isRegisteredRowWiseAnchorExtractorType(className: Class[_]): Boolean = {
    getCachedAdaptor(className) match {
      case Some(_: RowWiseAnchorExtractorUdfAdaptor) => true
      case _ => false
    }
  }

  def getRowWiseAnchorExtractorAdaptor(className: Class[_]): RowWiseAnchorExtractorUdfAdaptor = {
    getCachedAdaptor(className) match {
      case Some(adaptor: RowWiseAnchorExtractorUdfAdaptor) => adaptor
      case _ => throw new RuntimeException(s"No UDF adaptor defined for class ${className}")
    }
  }

  def isRegisteredDfWiseAnchorExtractorType(className: Class[_]): Boolean = {
    getCachedAdaptor(className) match {
      case Some(_: RowWiseAnchorExtractorUdfAdaptor) => true
      case _ => false
    }
  }

  def getDfWiseAnchorExtractorAdaptor(className: Class[_]): DfWiseAnchorExtractorUdfAdaptor = {
    getCachedAdaptor(className) match {
      case Some(adaptor: DfWiseAnchorExtractorUdfAdaptor) => adaptor
      case _ => throw new RuntimeException(s"No UDF adaptor defined for class ${className}")
    }
  }

  private def findRegisteredParent(className: Class[_]): Option[Class[_]] = {
    registeredUdfAdaptors.keys.find(registeredClass => registeredClass.isAssignableFrom(className))
  }

  private def getCachedAdaptor(className: Class[_]): Option[UdfAdaptor] = {
    adaptorSubclassCache.getOrElseUpdate(className, findRegisteredParent(className).flatMap(registeredUdfAdaptors.get))
  }

}
