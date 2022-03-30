package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.offline.config.datasourceprovider.SecretStorage

@CaseClassDeserialize()
case class UserPassCredential(var user: String = "", var pass: String = "") extends Credential {
  override def init(ss: SecretStorage, path: String, name: String): Unit = {
    user = ss.getSecret(path, name, "user")
    pass = ss.getSecret(path, name, "pass")
  }
}
