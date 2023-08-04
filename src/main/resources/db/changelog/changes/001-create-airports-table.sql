--liquibase formatted sql

--changeset gustavs:1


CREATE TABLE airports
(
    airport VARCHAR(255) NOT NULL PRIMARY KEY ,
    country VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL ,
)