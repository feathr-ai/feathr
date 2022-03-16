package com.linkedin.feathr.offline.config.datasourceprovider

import com.linkedin.feathr.offline.config.datasourceprovider.credential.Credential

trait CredentialRegistry {
  def getCredential(name: String): Credential
}
