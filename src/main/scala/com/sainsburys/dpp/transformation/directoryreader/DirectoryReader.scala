package com.sainsburys.dpp.transformation.directoryreader
import java.io.File

import com.sainsburys.dpp.transformation.Loggable

/**
  * Returns a list of source files within a directory or zip archive
  *
  * @author Dawud Rahman (dawud.rahman@sainsburys.co.uk)
  * @version 1.0
  * @since 06/01/2017
  */
class DirectoryReader (private val path: String) extends Loggable {

  logger.info("DirectoryReader started...")

  val sourcePath = new File(path)
  if(sourcePath.exists()) {
    if(sourcePath.isDirectory) {
      val numFiles = sourcePath.listFiles().length
      logger.info("Found {} files in {}", numFiles, sourcePath.getAbsolutePath)
    } else {
      logger.info("Found source file: {}", sourcePath.getAbsolutePath)
    }
  } else {
    logger.error("Source does not exist: {}", path)
  }

  /*
  Return list of source files
   */
  def getListOfFiles():List[File] = {
    logger.info("Processing files in {}", path)

    if (sourcePath.exists && sourcePath.isDirectory) {
      sourcePath.listFiles
        .toList
      //TODO: Add handle zip
    } else {
      List[File]()
    }
  }

  /*
  Create output director
   */
  def createOutputDirectory(path :String) = {
    val outputPath = new File(path);
    if(!outputPath.exists()) {
      outputPath.mkdir
      outputPath.setWritable(true)
    }
  }


  /*
  TODO: Unzip source files to temp folder
   */
  def getListOfUnzippedFiles():List[File] = {
    getListOfFiles()
  }


}


object DirectoryReader {

  def apply(path: String): DirectoryReader =
      new DirectoryReader(path)

  // TODO: Review - remove
  def main(args: Array[String]): Unit = {
    val path = "testSamples"
    val source = new DirectoryReader(path).getListOfFiles()
    source.foreach(file => println(file.getPath))

  }

}