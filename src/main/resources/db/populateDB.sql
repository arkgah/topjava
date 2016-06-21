DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (id, user_id, date_time, description, calories) VALUES
  (200000, 100000, '2016-06-19 09:00:15', 'Каша', 150),
  (200001, 100000, '2016-06-19 14:10:30', 'Суп рыбный', 350),
  (200002, 100000, '2016-06-19 14:25:10', 'Пюре картофельное с сосиской', 600),
  (200003, 100000, '2016-06-19 14:45:44', 'Мороженое', 400),
  (200004, 100000, '2016-06-19 18:55:15', 'Макароны с сыром', 500),
  (200005, 100000, '2016-06-19 19:05:16', 'Чай с булочкой', 250);

INSERT INTO meals (id, user_id, date_time, description, calories) VALUES
  (300000, 100001, '2016-06-20 09:10:00', 'Завтрак', 500),
  (300001, 100001, '2016-06-20 14:10:00', 'Обед', 1400),
  (300002, 100001, '2016-06-20 19:10:00', 'Ужин', 300);



