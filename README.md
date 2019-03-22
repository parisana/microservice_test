# Swagger Light Java Server

# Running:
[Make sure that port 8080 and 8085 are free]
-   Extract the zip file
-   go inside the folder where the jars are located
-   run the following commands in different terminals
    $ java -jar consumer-service-1.0.1.jar
    $ java -jar demo-rest-1.0.1.jar
- now you may make rest query to
    curl --header "Content-Type: application/json"   --request POST -k   http://localhost:8080/post?your_name=123

### Setup:

# Kafka
-   topic: test
-   broker: localhost:9092

# mongodb
-   db name: test_db
-   port: 27017
-   commands for mongo:
-   $use test_db
-   $db.createUser(
       {
         user: "user",
         pwd: "password",
         roles: [ "readWrite", "dbAdmin" ]
       }
    )

# Logs are logged in the file target/test.log

# Request example:
-curl --header "Content-Type: application/json"   --request POST -k   http://localhost:8080/post?your_name=123
-   returns success on success
-   check the log file: target/test.log and database, a new entry should be created.


