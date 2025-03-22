--liquibase formatted sql
--changeset apenasolinco:202503201743
--comment: boards table create

CREATE TABLE Boards(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL
);

--rollback DROP TABLE Boards