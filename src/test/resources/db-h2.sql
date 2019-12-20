drop table IF EXISTS account;
drop table IF exists account_transaction;


create TABLE account (
 id INT IDENTITY NOT NULL PRIMARY KEY,
 owner varchar(256),
 balance NUMERIC(10,2),
  version int DEFAULT 0

);
create TABLE account_transaction(
 id INT IDENTITY NOT NULL PRIMARY KEY,
 sent_from INT,
 send_to INT,
 amount NUMERIC(10,2),
 status BIT DEFAULT 0,
 version int DEFAULT 0,
);

insert into account(owner,balance) values ('test1',100000);
insert into account(owner,balance) values ('test2',100000);
insert into account(owner,balance) values ('test3',100000);
insert into account(owner,balance) values ('test4',100000);
insert into account(owner,balance) values ('test5',100000);
insert into account(owner,balance) values ('test6',100000);
insert into account(owner,balance) values ('test7',100000);
insert into account(owner,balance) values ('test8',100000);
