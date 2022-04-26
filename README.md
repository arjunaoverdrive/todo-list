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
  java -jar "C:\Users\Igor\Desktop\ToDoList-1.0-SNAPSHOT.jar"  
Remember to replace the path to the jar file with the actual address. 
6. After the first start, comment the #spring.jpa.hibernate.ddl-auto=create, or set the value to none: spring.jpa.hibernate.ddl-auto=none
