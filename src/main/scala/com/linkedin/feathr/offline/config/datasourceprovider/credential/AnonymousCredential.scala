package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize

@CaseClassDeserialize()
case class AnonymousCredential() extends Credential
