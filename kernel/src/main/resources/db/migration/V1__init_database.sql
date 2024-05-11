create schema if not exists magazine_schema;

create table if not exists product (
                                       id varchar(36) not null primary key,
                                       name varchar(50) not null unique,
                                       description varchar(200) not null,
                                       price    numeric not null
);

create table if not exists roles (
                                     id serial not null primary key,
                                     name varchar(50) not null
);

create table if not exists product_user (
                                            id   varchar(36) primary key not null,
                                            user_id varchar(36) unique not null,
                                            first_name varchar(50) not null,
                                            last_name varchar(50) not null,
                                            password varchar(50) not null,
                                            email varchar(50) not null
);

create table if not exists product_user_roles (
                                                  product_user_id varchar(36) not null,
                                                  role_id serial not null,
                                                  primary key (product_user_id, role_id),
                                                  foreign key (product_user_id) references product_user (id),
                                                  foreign key (role_id) references roles (id)
);

insert into product(id, name, description, price) values
                                                      ('11f6d793-f886-4a19-8fe9-75ec7394f26f', 'water', 'basic pure water', 50),
                                                      ('21f6d793-f886-4a19-8fe9-75ec7394f26f', 'pineapple', 'basic red pineapple', 200),
                                                      ('31f6d793-f886-4a19-8fe9-75ec7394f26f', 'pen', 'basic red pen', 15),
                                                      ('41f6d793-f886-4a19-8fe9-75ec7394f26f', 'pencil', 'basic black pencil', 10),
                                                      ('51f6d793-f886-4a19-8fe9-75ec7394f26f', 'apple', 'basic red apple', 100);

insert into product_user(id, user_id, first_name, last_name, password, email) values
                                                                                  ('1116d793-f886-4a19-8fe9-75ec7394f26f', '1111d793-f886-4a19-8fe9-75ec7394f26f', 'Testoy', 'Testev', '123', 'soap@bath.org'),
                                                                                  ('2116d793-f886-4a19-8fe9-75ec7394f26f', '2111d793-f886-4a19-8fe9-75ec7394f26f', 'Testoy2', 'Testev2', '123', 'soap@bath.org');

insert into roles(id, name) values
                                (1, 'ADMIN'),(2,'USER') ;

insert into product_user_roles(product_user_id, role_id) values
                                                             ('1116d793-f886-4a19-8fe9-75ec7394f26f', 1),
                                                             ('2116d793-f886-4a19-8fe9-75ec7394f26f', 2);