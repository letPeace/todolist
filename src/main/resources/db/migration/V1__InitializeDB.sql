create sequence hibernate_sequence start 1 increment 1;

create table category (
    id int8 not null, 
    created_date timestamp, 
    modified_date timestamp, 
    title varchar(255) not null, 
    user_id int8, 
    primary key (id)
);

create table task (
    id int8 not null, 
    completed boolean, 
    created_date timestamp, 
    modified_date timestamp, 
    text varchar(255) not null, 
    category_id int8, 
    user_id int8, 
    primary key (id)
);     

create table user_role (
    user_id int8 not null, 
    roles varchar(255) not null
);

create table usr (
    id int8 not null, 
    created_date timestamp, 
    password varchar(255) not null, 
    username varchar(255) not null, 
    primary key (id)
);

alter table if exists category 
    add constraint category_to_user_foreign_key 
    foreign key (user_id) references usr;

alter table if exists task 
    add constraint task_to_category_foreign_key 
    foreign key (category_id) references category;

alter table if exists task 
    add constraint task_to_user_foreign_key 
    foreign key (user_id) references usr;

alter table if exists user_role 
    add constraint user_role_to_user_foreign_key 
    foreign key (user_id) references usr;