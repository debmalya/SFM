# SFM
String frequency manager.

# Requirement
1. SFM shall process log file in a configurable local folder. Configuration can be done using property sfm.folder property in src/main/resources/application.properties
2. Keep track of how many times a particular string has appeared in those log files. For this I used [MapDB](http://www.mapdb.org/)
3. Provided an endpoint API to make use of this frequency of String http://localhost:8080/isStringValid?String={string}

# How to run
1. execute run.sh or mvn spring-boot:run
2. Store log files in the specified format in the folder configured in application.properties

# Basic Testing
It has a mapdb embedded database at location src/main/resources/db/WordRegister24Hours.db . After successfully running the application, for initial testing can call http://localhost:8080/isStringValid?string=never response will be {"response":"true"} , as the word "never" did not occur. http://localhost:8080/isStringValid?string=always for this response will be {"response":"false"} , as it has more than 5 occurrences witin last 24 hours.

To start fresh can stop the application delete file src/main/resources/db/WordRegister24Hours.db and play around.

