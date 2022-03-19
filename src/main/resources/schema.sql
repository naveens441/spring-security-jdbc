drop table if exists users;

create table users(
    username varchar(50) not null primary key,
    password varchar(500) not null,
    enabled boolean not null
);

drop table if exists authorities;
create table authorities(
    username varchar(255) not null,
    authority varchar(50) not null,
    foreign key (username) references users(username)
 );

 create unique index ix_auth_username on authorities (username, authority);

