# ToDoList
ToDoList Java Spring Boot application  

# Technologies used:
**Java, Spring Boot, Spring Security, Spring Data Jpa, MySQL, JavaScript (jQuery), HTML, CSS, Thymeleaf**

Installation steps:
1. Install MySQL v 8.x. Create a schema and a user with admin rights for this schema. 
2. Use mvn package to build a .jar file.  
3. Open the application.yml file, and replace the default values with the valid the DB credentials in the
spring:datasource parameters.  
Besides, remember to uncomment #spring.jpa.hibernate.ddl-auto=create when starting the application for the first time so that the db is initialized.
4. Specify the server port. Otherwise, the app will use port 8888.
5. Create a bat file to run the app without having to open the command prompt. 
Insert the following content:   
  chcp 1251  
  start http://localhost:8888  
  java -jar "path-to-file\ToDoList-1.0-SNAPSHOT.jar"  
Remember to replace the path to the jar file with the actual address. 
6. After the first start, comment the #spring.jpa.hibernate.ddl-auto=create, or set the value to none: spring.jpa.hibernate.ddl-auto=none

Installation steps for Docker containers:  
This approach implies that you run both the MySQL and the ToDoList in two separate containers.
1. Pull the image by running the docker pull arjunaoverdrive/todo-list:latest command
2. Pull the mysql image. Make sure it's 8.x version.
3. The application is configured to use the 'root' mysql user with the 'root' password and to connect to the 'todolistlib' database. These settings are specified in the src/main/resources/application.yml, so you can modify them.
4. Docker commands can be taken from docker-scripts