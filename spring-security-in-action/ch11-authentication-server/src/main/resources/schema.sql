create table if not exists authentication_db.users (
    username VARCHAR(45) not null primary key,
    password TEXT not null
);

create table if not exists authentication_db.otp (
    username VARCHAR(45) not null,
    code VARCHAR(45) null,
    primary key (username)
);