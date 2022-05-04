FROM openjdk:14
MAINTAINER Igor Klimov
COPY target/ToDoList-1.0-SNAPSHOT.jar /usr/src/todo-list.jar
ENTRYPOINT ["java", "-jar", "/usr/src/todo-list.jar"]

