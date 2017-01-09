package Xml2CsvTransformer.config

import com.sainsburys.dpp.transformation.Xml2CsvTransformer
import com.sainsburys.dpp.transformation.config.ConfigPropertiesFactory
import org.scalatest.FunSuite

class ConfigPropertiesFactory$Test extends FunSuite {

  test("testLoadConfigFromFile") {

    val args = new Array[String](1)
    args(0) = "config=application.conf"
    val configProperties = ConfigPropertiesFactory.load(args)
    assert(configProperties.xslStylesheetR10 == "xslStylesheetR10-test")
    assert(configProperties.xmlSourcePathR10 == "xmlSourcePathR10-test")
    assert(configProperties.csvOutputPathR10 == "csvOutputPathR10-test")
  }

  test("testLoadConfigFromArguments") {

    val args = new Array[String](4)
    args(0) = "xsl.stylesheet.r10=csvOutputPathR10-new"
    args(1) = "xml.source.path.r10=csvOutputPathR10-new"
    args(2) = "csv.output.path.r10=csvOutputPathR10-new"
    args(3) = "config=application.conf"
    val configProperties = ConfigPropertiesFactory.load(args)
    assert(configProperties.xslStylesheetR10 == "csvOutputPathR10-new")
    assert(configProperties.xmlSourcePathR10 == "csvOutputPathR10-new")
    assert(configProperties.csvOutputPathR10 == "csvOutputPathR10-new")
  }

}
