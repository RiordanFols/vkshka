INSERT INTO usr (id, is_active, username, gender, avatar_filename, email, password, name, surname, status, birthday, last_online)
VALUES (1, TRUE, 'fedor', 'MALE', '13f46ce2-f0a0-41be-8023-fc7e558a6c65.Fedor.jpg', 'fedor@gmail.com', '123456', 'Дядя', 'Фёдор', 'Я ничей. Я сам по себе мальчик. Свой собственный', '1966-09-23', '1970-01-25 23:10:12+03'),
       (2, TRUE, 'galchonok', 'UNDEFINED','cffb996c-d608-4a8a-b15a-42a41dd46813.Galchonok.jpg', 'galchonok@gmail.com' , '123456', 'Галчонок', 'Простоквашинский', 'Кто там?', '1976-10-08', '1970-01-25 23:10:12+03'),
       (3, TRUE, 'pechkin', 'MALE','db50aaf5-a660-4233-bf9a-e2d2176c2099.Pechkin.jpg', 'pechkin@gmail.com', '123456', 'Почтальон', 'Печкин', 'Я, может, только жить начинаю: на пенсию перехожу', '1920-01-23', '1970-01-25 23:10:12+03'),
       (4, TRUE, 'dog', 'UNDEFINED','ed99779a-55d5-42b4-afab-569a651b8435.Sharik.jpg', 'sharik@gmail.com', '123456', 'Шарик', '', 'Пойти, что-ль, пожрать. Ну их в болото', '1971-04-11', '1970-01-25 23:10:12+03'),
       (5, TRUE, 'cat', 'UNDEFINED','2cf6dd7a-7d8f-4f89-a94d-16eee7793859.Matroskin.jpg', 'matroskin@gmail.com', '123456', 'Кот', 'Матроскин', 'Неправильно ты, Дядя Федор, колбасу ешь', '1972-09-12', '1970-01-25 23:10:12+03');

INSERT INTO user_role(user_id, role)
VALUES (1, 'USER'),
       (2, 'USER'),
       (3, 'USER'),
       (4, 'USER'),
       (5, 'USER');