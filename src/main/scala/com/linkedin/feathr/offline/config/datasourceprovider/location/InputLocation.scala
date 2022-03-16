package com.linkedin.feathr.offline.config.datasourceprovider.location

import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.linkedin.feathr.offline.config.datasourceprovider.credential.{AadAppCredential, AccessKeyCredential, AnonymousCredential, ConnectionStringCredential, UserPassCredential}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = classOf[FixedPath])
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[FixedPath], name = "fixed_path"),
    new JsonSubTypes.Type(value = classOf[Jdbc], name = "jdbc"),
  ))
trait InputLocation {

}
