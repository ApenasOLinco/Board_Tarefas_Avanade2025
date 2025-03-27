--liquibase formatted sql
--changeset apenasolinco:202503271754
--comment: remove field title from Blocks table

ALTER TABLE Blocks
DROP COLUMN title;

--rollback ALTER TABLE Blocks ADD title VARCHAR(255)