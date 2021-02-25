SET NAMES utf8;
DROP DATABASE IF EXISTS iptest;
CREATE DATABASE if not exists iptest CHARACTER SET utf8 COLLATE utf8_bin;
USE iptest;

CREATE TABLE roles(
	id BIGINT NOT NULL PRIMARY KEY,
	name VARCHAR(10) NOT NULL UNIQUE
);
CREATE TABLE users(
	id BIGINT NOT NULL auto_increment PRIMARY KEY,
	login VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL,
	password VARCHAR(256) NOT NULL,
    salt VARBINARY(500) NOT NULL,
	role_id BIGINT NOT NULL REFERENCES roles(id)
);
CREATE TABLE products
(
    id BIGINT NOT NULL auto_increment PRIMARY KEY,
    name VARCHAR(120) NOT NULL
);
CREATE TABLE plans
(
    id BIGINT NOT NULL auto_increment PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    price DOUBLE NOT NULL,
    product_id BIGINT,
    CONSTRAINT FK_plans_products FOREIGN KEY (product_id)
        REFERENCES products (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE TABLE accounts
(
    id BIGINT NOT NULL auto_increment PRIMARY KEY,
    active BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_accounts_users FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE TABLE payments
(
    id BIGINT NOT NULL auto_increment PRIMARY KEY,
    amount DOUBLE NOT NULL ,
    date TIMESTAMP,
    name VARCHAR(255),
    account_id BIGINT NOT NULL ,
    CONSTRAINT FK_payments_account FOREIGN KEY (account_id)
        REFERENCES accounts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE TABLE accounts_plans
(
    account_id bigint NOT NULL,
    plans_id BIGINT NOT NULL,
    CONSTRAINT accounts_plans_pkey PRIMARY KEY (account_id, plans_id),
    CONSTRAINT FK_account_plans FOREIGN KEY (plans_id)
        REFERENCES plans (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT FK_plans_account FOREIGN KEY (account_id)
        REFERENCES accounts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
