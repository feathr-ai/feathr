package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize

@CaseClassDeserialize()
case class AccessKeyCredential(key: String = "") extends Credential
