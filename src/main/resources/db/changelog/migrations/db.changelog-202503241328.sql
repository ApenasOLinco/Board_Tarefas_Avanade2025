--liquibase formatted sql
--changeset apenasolinco:202503241328
--comment: kind column type alter

ALTER TABLE Boards_Columns
ALTER COLUMN kind TYPE VARCHAR(8);

--rollback ALTER TABLE Boards_Columns ALTER COLUMN kind TYPE VARCHAR(7)