delete from task;
insert into task(id, deadline, description, name, user_id) values
( 1,'2022-05-14 18:00:00','Переписать на микросервисах. Сделать сервис для юзеров, сервис для задач','ToDoList', '1'),
(2,'2022-05-15 18:00:00','Buy food', 'ToDoList', '1' );

INSERT INTO todolistlibtest.hibernate_sequence (next_val) VALUES (10);