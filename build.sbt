name := "xmlToCsv-transformer"

version := "1.0"

scalaVersion := "2.10.6"

libraryDependencies ++= Seq(
  "net.sf.saxon" % "Saxon-HE" % "9.5.1-5",
  // https://mvnrepository.com/artifact/net.sf.saxon/Saxon-HE
  "com.jcraft" % "jsch" % "0.1.48" ,
  "com.typesafe" % "config" % "1.3.1",
  "org.slf4j" % "slf4j-simple" % "1.7.21",
  "org.slf4j" % "slf4j-api" % "1.7.21" ,
  "org.mockito" % "mockito-all" % "1.9.5",
  "org.scalatest" % "scalatest_2.10" % "3.0.1" % "test",
  "com.opencsv" % "opencsv" % "3.8"
)
