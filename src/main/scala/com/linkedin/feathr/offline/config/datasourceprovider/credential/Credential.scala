package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

// TODO: Dynamical sub typing, need to write a customized deserializer
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = classOf[AnonymousCredential])
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[AnonymousCredential], name = "anonymous"),
    new JsonSubTypes.Type(value = classOf[AccessKeyCredential], name = "key"),
    new JsonSubTypes.Type(value = classOf[AadAppCredential], name = "aad"),
    new JsonSubTypes.Type(value = classOf[ConnectionStringCredential], name = "connstr"),
    new JsonSubTypes.Type(value = classOf[UserPassCredential], name = "userpass"),
  ))
trait Credential {

}
