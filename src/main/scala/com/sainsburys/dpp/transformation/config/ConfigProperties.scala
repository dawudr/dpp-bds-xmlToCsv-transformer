package com.sainsburys.dpp.transformation.config

import com.typesafe.config.ConfigFactory

/**
  * Properties for stylesheet location, source and output paths
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
sealed class ConfigProperties (var xslStylesheetR10:String,
                                       var xmlSourcePathR10:String,
                                       var csvOutputPathR10:String)


object ConfigProperties {

  def apply(configFilePath:String): ConfigProperties = {
    val configFile = ConfigFactory.load(configFilePath)

    new ConfigProperties(
      configFile.getString("xsl.stylesheet.r10"),
      configFile.getString("xml.source.path.r10"),
      configFile.getString("csv.output.path.r10")
    )
  }

  def apply(): ConfigProperties = new ConfigProperties("","","")
}