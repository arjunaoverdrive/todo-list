# ToDoList
ToDoList Java Spring Boot application
#Technologies used:  
**Java, Spring Boot, Spring Security, Spring Data Jpa, MySQL, JavaScript (jQuery), HTML, CSS, Thymeleaf**

Installation steps:
1.Install MySQL v 8.x. Create a schema and a user with admin rights for this schema.  
2.Open the application.properties file, and replace the default values with the valid the DB credentials in 
spring.datasource parameters.  
3.Specify the server port. Otherwise, the app will use port 8888.  
4.Create a bat file to run the app without having to open the command prompt. 
Insert the following content:   
  chcp 1251  
  start http://localhost:8888  
  java -jar "C:\Users\Igor\Desktop\ToDoList-1.0-SNAPSHOT.jar"
Remember to replace the path to the jar file with the actual address. 

