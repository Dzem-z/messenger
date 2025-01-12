# messenger

This is a simple messaging app written using Spring Boot framework and React.js. It allows users to message each other in real-time.

![sample](img/chat.png)

### License
This project is developed under an MIT license.

### Installation
In order for the server to start, the database is needed. Connection with the database can be configured in the [application.properties](src/main/resources/application.properties) file. By default, it listens on localhost:3306 MySQL port and uses springdb as a database. The default username is "spring" and the password is "password". Here is a sample config using docker:<br /><br />
First, create a MySQL container:<br />
```
docker run --rm --name spring-mysql -e MYSQL_ROOT_PASSWORD=toor -e MYSQL-NATIVE-PASSWORD=ON -p 3306:3306 --mount source=springdb,target=/var/lib/mysql -d mysql
```

Then execute the [prepare_database](prepare_database.sql) script. For example: <br />
```
mysql -u root -h 127.0.0.1 -P 3306 -p < prepare_database.sql
```
Next, to build frontend first run:
```
npm install
```
Then:
```
npm run build
```
Copy the contents to static directory:
```
cp -r frontend/build/* src/main/resources/static
```
Finally, to run the application, execute the maven script:
```
./mvnw spring-boot:start
```
