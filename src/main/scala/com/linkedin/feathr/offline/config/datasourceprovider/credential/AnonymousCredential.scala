package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.offline.config.datasourceprovider.SecretStorage

@CaseClassDeserialize()
case class AnonymousCredential() extends Credential {
  override def init(ss: SecretStorage, path: String, name: String) {}
}
