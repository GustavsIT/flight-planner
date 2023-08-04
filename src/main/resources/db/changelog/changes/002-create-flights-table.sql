--liquibase formatted sql

--changeset gustavs:1

CREATE TABLE flights
(
    id SERIAL PRIMARY KEY,
    airport_from VARCHAR(255) NOT NULL,
    airport_to VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    FOREIGN KEY (airport_from) REFERENCES airports(airport) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (airport_to) REFERENCES airports(airport) ON DELETE CASCADE ON UPDATE CASCADE,
)