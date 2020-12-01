# User Management API

## Running Project

### Standalone

Build the project with `./gradlew build` or `./gradlew.bat build`.

Run the project with `java -jar ./build/libs/*.jar`

### Docker

To deploy and run use `./run.sh build_start` or `./run.bat build_start`.

All services of the project are run as docker containers. The run script offers the next tasks:

 * `build_start`. Build the whole project, recreate User Management and tail the logs of all the containers.
 * `start`. Start the dockerised environment without building the project and tail the logs of all the containers.
 * `stop`. Stop the dockerised environment.
 * `tail`. Tail the logs of all the containers.

## Connect to H2 Database

### Connect to H2 Console

To use H2 Console go to http://localhost:8080/h2-console.
- URL: jdbc:h2:file:./build/h2/db/user_management
- User: admin
- Password: admin

### Connect to H2 via TCP

The service exposes an H2 console on port 8082.
- URL: jdbc:h2:tcp://localhost:8082/./build/h2/db/user_management
- User: admin
- Password: admin
