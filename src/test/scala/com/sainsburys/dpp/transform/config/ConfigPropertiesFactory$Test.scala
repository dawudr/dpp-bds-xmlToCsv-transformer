package Xml2CsvTransformer.config

import com.sainsburys.dpp.transform.TransformSession
import com.sainsburys.dpp.transform.config.ConfigPropertiesFactory
import org.scalatest.FunSuite

class ConfigPropertiesFactory$Test extends FunSuite {

  test("Given application.conf config file test load config from properties") {

    val args = new Array[String](1)
    args(0) = "config=application.conf"
    val configProperties = ConfigPropertiesFactory.load(args)
    assert(configProperties.xslTemplate == "src/test/resources/domain/purchases/xsl/r10_pipeline.xsl")
    assert(configProperties.xmlPath == "src/test/resources/xml")
    assert(configProperties.transformOutputPath == "output/test")
  }

  test("Given properties as arguments test Load Config From Arguments") {

    val args = new Array[String](4)
    args(0) = "r10.xsl.template=xslTemplate-new"
    args(1) = "r10.xml.source.path=xmlPath-new"
    args(2) = "r10.transform.output.path=transformOutputPath-new"
    args(3) = "config=application.conf"
    val configProperties = ConfigPropertiesFactory.load(args)
    assert(configProperties.xslTemplate == "xslTemplate-new")
    assert(configProperties.xmlPath == "xmlPath-new")
    assert(configProperties.transformOutputPath == "transformOutputPath-new")
  }

}
