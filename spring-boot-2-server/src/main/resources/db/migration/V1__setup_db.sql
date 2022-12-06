create table users
(
    id     bigint generated always as identity primary key,
    "name" varchar(100) not null
);

create table groups
(
    id     bigint generated always as identity primary key,
    "name" varchar(100) not null
);

create table user_groups
(
    id       bigint generated always as identity primary key,
    user_id  bigint not null,
    group_id bigint not null
);

create table files
(
    id               bigint generated always as identity primary key,
    "name"           varchar(100) not null,
    user_id          bigint       not null,
    group_id         bigint       not null,
    user_permission  varchar(10)  not null,
    group_permission varchar(10)  not null,
    other_permission varchar(10)  not null
);

alter table users
    add constraint user_name_unique unique ("name");

alter table groups
    add constraint groups_name_unique unique ("name");

alter table user_groups
    add constraint user_groups_user_id_foreign_key foreign key (user_id) references users (id);
alter table user_groups
    add constraint user_groups_group_id_foreign_key foreign key (group_id) references groups (id);

alter table files
    add constraint file_user foreign key (user_id) references users (id);
alter table files
    add constraint file_group foreign key (group_id) references groups (id);
alter table files
    add constraint file_name_unique unique ("name");
