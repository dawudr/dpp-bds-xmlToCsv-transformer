package com.sainsburys.dpp.transform

import org.slf4j.LoggerFactory
/**
  * App Logging
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
trait Loggable {
  protected lazy val logger = LoggerFactory.getLogger(getClass)
}
