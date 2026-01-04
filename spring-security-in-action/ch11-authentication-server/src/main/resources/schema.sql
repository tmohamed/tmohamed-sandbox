create table if not exists users (
    username VARCHAR(45) not null primary key,
    password TEXT not null
);

create table if not exists otp (
    username VARCHAR(45) not null,
    code VARCHAR(45) null,
    primary key (username)
);