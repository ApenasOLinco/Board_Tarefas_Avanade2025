--liquibase formatted sql
--changeset apenasolinco:202503201743
--comment: blocks table create

CREATE TABLE Blocks(
	id BIGSERIAL PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	created_reason varchar(255) NOT NULL,
	closed_at TIMESTAMP NULL,
	closed_reason varchar(255) NULL,
	card_id BIGINT NOT NULL,
	CONSTRAINT cards__blocks_fk FOREIGN KEY (card_id) REFERENCES Cards(id) ON DELETE CASCADE
);

--rollback DROP TABLE Blocks