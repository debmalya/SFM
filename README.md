# SFM
String frequency manager.

# Requirement
1. SFM shall process log file in a configurable local folder. Configuration can be done using property sfm.folder property in src/main/resources/application.properties
2. Keep track of how many times a particular string has appeared in those log files.For this used [MapDB](http://www.mapdb.org/)
3. Provided an endpoint API to make use of this frequency of String http://localhost:8080/isStringValid?String={string}

# How to run
1. execute run.sh or mvn spring-boot:run

