package com.linkedin.feathr.offline.config.datasourceprovider.location

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize

@CaseClassDeserialize()
case class FixedPath(path: String = "") extends InputLocation
