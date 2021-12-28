# ToDoList
ToDoList Java Spring Boot application  

Technologies used: Java, Spring Boot, Spring Security, MySQL, JavaScript (jQuery), HTML, CSS, Thymeleaf  

Installation steps:  

1. Install MySQL v 8.x. Create a schema and a user with admin rights for this schema.
2. Build the app jar file. Use mvn package.  
3. Open the application.properties file, and replace the default values with the valid the DB credentials in spring.datasource parameters.
4. Specify the server port. Otherwise, the app will use port 8888. 
5. Create a bat file to run the app without having to open the command prompt. Insert the following content:  
  chcp 1251  
  start http://localhost:8888  
  java -jar "C:\Users\Igor\Desktop\ToDoList_1.5.jar". Remember to replace the path to the jar file with the actual address.  
The example above is given for Windows, so it can be run in a Linux terminal like this: java -jar <Path-to-file>.  
  
5.Once the bat file is created, you can run the app by simply clicking it. 
