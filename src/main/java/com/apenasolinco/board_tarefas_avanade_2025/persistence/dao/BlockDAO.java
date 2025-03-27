package com.apenasolinco.board_tarefas_avanade_2025.persistence.dao;

import static com.apenasolinco.board_tarefas_avanade_2025.persistence.converter.LocalDateTimeConverter.toTimestamp;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockDAO {

	private final Connection connection;

	public void block(final Long cardId, String reason) throws SQLException {
		var sql = "INSERT INTO Blocks (created_at, created_reason, card_id) VALUES (?, ?, ?)";

		try (var statement = connection.prepareStatement(sql)) {
			var i = 1;
			statement.setTimestamp(i++, toTimestamp(LocalDateTime.now()));
			statement.setString(i++, reason);
			statement.setLong(i, cardId);

			statement.executeUpdate();
		}
	}

	public void unblock(final Long cardId, String reason) throws SQLException {
		// @formatter:off
		var sql = 
			"""
			UPDATE Blocks
			SET
				closed_at = ?,
				closed_reason = ?
			WHERE
				card_id = ?
			AND
				closed_reason IS NULL
			""";
		// @formatter:on

		try (var statement = connection.prepareStatement(sql)) {
			var i = 1;
			statement.setTimestamp(i++, toTimestamp(LocalDateTime.now()));
			statement.setString(i++, reason);
			statement.setLong(i, cardId);

			statement.executeUpdate();
		}
	}
}
