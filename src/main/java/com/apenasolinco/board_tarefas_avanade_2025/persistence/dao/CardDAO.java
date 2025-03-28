package com.apenasolinco.board_tarefas_avanade_2025.persistence.dao;

import static com.apenasolinco.board_tarefas_avanade_2025.persistence.converter.LocalDateTimeConverter.toLocalDateTime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import com.apenasolinco.board_tarefas_avanade_2025.dto.CardDetailsDTO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.CardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardDAO {

	private final Connection connection;

	public CardEntity insert(final CardEntity entity) throws SQLException {
		var sql = "INSERT INTO Cards (title, description, board_column_id) VALUES (?, ? , ?) RETURNING id";

		try (var statement = connection.prepareStatement(sql)) {
			var i = 1;
			statement.setString(i++, entity.getTitle());
			statement.setString(i++, entity.getDescription());
			statement.setLong(i, entity.getColumn().getId());

			var inserted = statement.execute();
			var result = statement.getResultSet();

			if (inserted && Objects.nonNull(result) && result.next()) {
				var id = result.getLong("id");
				entity.setId(id);
			}
		}

		return entity;
	}

	public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
		// @formatter:off
		var sql = 
			"""
			SELECT 
				c.id AS card_id, 
				c.title AS card_title,
				c.description AS card_desc,
				b.created_at AS block_date,
				b.created_reason AS block_reason,
				c.board_column_id AS c_bcid,
				bc.name as column_name,
				(
					SELECT
						COUNT(sub_b.id)
					FROM
						Blocks sub_b
					WHERE
						sub_b.card_id = c.id
				) AS blocks_amount
			FROM 
				Cards c
			LEFT JOIN
				Blocks b
			ON
				c.id = b.card_id
			AND
				b.closed_at IS NULL
			INNER JOIN
				Boards_Columns bc
			ON
				bc.id = c.board_column_id
			WHERE
				c.id = ?
			""";
		// @formatter:on

		try (var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id);
			var result = statement.executeQuery();

			if (result.next()) {
				var dto = new CardDetailsDTO(result.getLong("card_id"), result.getString("card_title"),
						result.getString("card_desc"), Objects.nonNull(result.getString("block_reason")),
						toLocalDateTime(result.getTimestamp("block_date")), result.getString("block_reason"),
						result.getInt("blocks_amount"), result.getLong("c_bcid"), result.getString("column_name"));

				return Optional.of(dto);
			}
		}

		return Optional.empty();
	}

	public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
		// @formatter:off
		var sql =
			"""
			UPDATE
				Cards
			SET
				board_column_id = ?
			WHERE
				id = ?
			""";
		// @formatter:on

		try (var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, columnId);
			statement.setLong(2, cardId);

			statement.executeUpdate();
		}
	}
}
