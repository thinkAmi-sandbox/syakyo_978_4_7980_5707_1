# --- !Ups
create table people (
    id int auto_increment primary key,
    name varchar(255) not null,
    mail varchar(255) not null,
    tel varchar(255)
);

insert into people values (default, 'taro', 'taro@example.com', '03-2000-0000');
insert into people values (default, 'hanako', 'hanako@example.com', '03-2000-0001');
insert into people values (default, 'jiro', 'jiro@example.com', '03-2000-0002');

# --- !Downs
drop table people
