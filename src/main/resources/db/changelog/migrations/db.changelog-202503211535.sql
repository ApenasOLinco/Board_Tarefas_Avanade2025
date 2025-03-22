--liquibase formatted sql
--changeset apenasolinco:202503201743
--comment: boards_columns table create

CREATE TABLE Boards_Columns(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	order_ INT NOT NULL,
	kind VARCHAR(7) NOT NULL,
	board_id BIGINT NOT NULL,
	CONSTRAINT boards__boards_columns_fk FOREIGN KEY (board_id) REFERENCES Boards(id) ON DELETE CASCADE,
	CONSTRAINT id_order_uk UNIQUE (board_id, order_)
);

--rollback DROP TABLE Boards_Columns