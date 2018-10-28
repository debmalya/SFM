# SFM
String frequency manager.

# Requirement
1. SFM shall process log file in a configurable local folder. Configuration can be done using property sfm.folder property in src/main/resources/application.properties
2. Keep track of how many times a particular string has appeared in those log files. For this I used [MapDB](http://www.mapdb.org/)
3. Provided an endpoint API to make use of this frequency of String http://localhost:8080/isStringValid?String={string} If the string appears more than 5 times in the last 24 hours, return {"response":"false"}, else return {"response":"true"}

# How to run
1. execute run.sh or mvn spring-boot:run


# Configuration
## folder to be watched
* sfm.folder=./src/test/resources/logs

## file name pattern
* sfm.pattern=string-generation-[0-9]+.log

## file extension
* sfm.extension=.log

## more than below number of occurrences within last 24 hours will make it valid.
* sfm.wordCount=5

