********# dpp-bds-xmlToCsv-transformer

## Descriprion
Stand alone Scala application which transforms XML files into CSV format.

## Prerequisites
1. sbt 0.13.13
2. Java 8

## Download
```
git clone git@github.com:JSainsburyPLC/dpp-bds-xmlToCsv-transformer.git
```

## Build
```bash
cd /path_to_source
sbt package
```

## Run


### SBT 
```bash
cd /path_to_source
sbt run config=path_to_config_file arg2 arg3 ...
```
### Jar
```bash
java -jar /path_to_jar/dpp-bds-xmlToCsv-transformer_2.10-1.0.jar config=path_to_config_file  arg2 arg3 ...
```

## Config file

To read the configuration parameters https://github.com/typesafehub/config is being used so any format supported by them is implicitily supported.

| Property name | Description   |
| ------------- |:-------------:|
| s3AccessKey   | Amazon S3 access key  |
| s3SecretKey   | Amazon S3 secret key |
| s3BucketName   | Amazon S3 bucket name |
| s3KeyPrefix   | Defines the prefix for bucket's keys |
| sourceXmlFileFilter   | Define regex to filter the xml source files and time frame window. For example if we are looking for r10 xml files then the value would be 'r10*.xml'|
| sourceXslFilePath  | Define filepath for custom Xsl script to transform xml. |
| destinationCsvOutputFilePath   | Path on server where to output transformation  |

## Additional running arguments (Optional)
Any of the above config properties could be overridden by a runtime argument if it follows the pattern ```propName=propValue```
For example replacing ftpPath from the config file would look like:
```bash
java -jar /path_to_jar/dpp-bds-xmlToCsv-transformer_2.10-1.0.jar config=path_to_config_file
```
