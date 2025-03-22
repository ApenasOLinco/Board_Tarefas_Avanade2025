--liquibase formatted sql
--changeset apenasolinco:202503201743
--comment: cards table create

CREATE TABLE Cards(
	id BIGSERIAL PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	description varchar(255) NOT NULL,
	board_column_id BIGINT NOT NULL,
	CONSTRAINT boards_columns__cards_fk FOREIGN KEY (board_column_id) REFERENCES Boards_Columns(id) ON DELETE CASCADE
);

--rollback DROP TABLE Cards