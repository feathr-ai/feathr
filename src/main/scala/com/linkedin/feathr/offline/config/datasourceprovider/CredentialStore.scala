package com.linkedin.feathr.offline.config.datasourceprovider

import com.linkedin.feathr.offline.config.datasourceprovider.credential.Credential

trait SecretStorage {
  def getSecret(path: String, name: String, key: String): String
}

class ConfigSecretStorage extends SecretStorage {
  override def getSecret(path: String, name: String, key: String): String = ???
}

object CredentialStore {
  // TODO: KeyVault SecretStorage and more
  private object ss extends ConfigSecretStorage()

  def getSecretStorage: SecretStorage = ss

  def getCredential(name: String): Credential = ???
}