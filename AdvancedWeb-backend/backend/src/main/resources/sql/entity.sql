create table user(
userId int auto_increment primary key,
username varchar(255),
password varchar(255),
age int,
gender varchar(255)
);

create table history(
historyId int auto_increment primary key,
time TIMESTAMP,
userId int,
username varchar(255),
messageType varchar(255),
message varchar(255)
);

create table game(
gameId int auto_increment primary key,
step int,
users varchar(255),
time TIMESTAMP
);
