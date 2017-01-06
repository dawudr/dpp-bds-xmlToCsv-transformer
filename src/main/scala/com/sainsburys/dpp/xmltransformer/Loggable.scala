package com.sainsburys.dpp.xmltransformer

import org.slf4j.LoggerFactory

trait Loggable {
  protected lazy val logger = LoggerFactory.getLogger(getClass)
}
