package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.offline.config.datasourceprovider.SecretStorage

@CaseClassDeserialize()
case class AadAppCredential(var appId: String = "", var secret: String = "") extends Credential {
  override def init(ss: SecretStorage, path: String, name: String): Unit = {
    appId = ss.getSecret(path, name, "appId")
    secret = ss.getSecret(path, name, "secret")
  }
}
