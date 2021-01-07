# Message schema
# --- !Ups
create table "messages" (
    "id" int auto_increment primary key,
    "personId" int,
    "message" varchar(255) not null,
    "created_at" timestamp not null default current_timestamp
);

insert into "messages" values (default, 1, 'This is sample message', default);
insert into "messages" values (default, 2, 'Hello', default);
insert into "messages" values (default, 3, '秋映', default);

# --- !Downs
drop table "messages"
