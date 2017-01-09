package com.sainsburys.dpp.transformation.config

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
      if (argsAsMap.contains("xsl.stylesheet.r10"))
        configProperties.xslStylesheetR10 = argsAsMap("xsl.stylesheet.r10")

      if (argsAsMap.contains("xml.source.path.r10"))
        configProperties.xmlSourcePathR10 = argsAsMap("xml.source.path.r10")

      if (argsAsMap.contains("csv.output.path.r10"))
        configProperties.csvOutputPathR10 = argsAsMap("csv.output.path.r10")
    }

    lookForConfigArgs

    configProperties
  }
}
