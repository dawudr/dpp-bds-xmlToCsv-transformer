package com.sainsburys.dpp.transform.config

import com.typesafe.config.ConfigFactory

/**
  * Properties for stylesheet location, source and output paths
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
sealed class ConfigProperties (var xslTemplate:String,
                               var xmlPath:String,
                               var transformOutputPath:String)


object ConfigProperties {

  def apply(configFilePath:String): ConfigProperties = {
    val configFile = ConfigFactory.load(configFilePath)

    new ConfigProperties(
      configFile.getString("r10.xsl.template"),
      configFile.getString("r10.xml.source.path"),
      configFile.getString("r10.transform.output.path")
    )
  }

  def apply(): ConfigProperties = new ConfigProperties("","","")
}