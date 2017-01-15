package com.sainsburys.dpp.transform.config

object ConfigPropertiesFactory {

  def load (args: Array[String]) : ConfigProperties = {

    def argsToMap(args: Array[String]) : collection.immutable.Map[String, String] = {
      var argsAsMap = collection.mutable.Map[String, String]()
      args.foreach(arg => {
        val argParts = arg.split("=")
        if (argParts.size == 2) {
          argsAsMap += argParts{0} -> argParts{1}
        }
      })
      argsAsMap.toMap
    }

    val argsAsMap = argsToMap(args)

    val configProperties = argsAsMap.contains("config") match {
      case true => ConfigProperties(argsAsMap("config"))
      case false => ConfigProperties()
    }

    def lookForConfigArgs = {
      if (argsAsMap.contains("r10.xsl.template"))
        configProperties.xslTemplate = argsAsMap("r10.xsl.template")

      if (argsAsMap.contains("r10.xml.source.path"))
        configProperties.xmlPath = argsAsMap("r10.xml.source.path")

      if (argsAsMap.contains("r10.transform.output.path"))
        configProperties.transformOutputPath = argsAsMap("r10.transform.output.path")
    }

    lookForConfigArgs

    configProperties
  }
}
