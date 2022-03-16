package com.linkedin.feathr.offline.config.datasourceprovider.location

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize

@CaseClassDeserialize()
case class Jdbc(url: String, tableName: String, credentialName: String) extends InputLocation
