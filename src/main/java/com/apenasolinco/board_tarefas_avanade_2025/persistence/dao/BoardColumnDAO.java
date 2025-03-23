package com.apenasolinco.board_tarefas_avanade_2025.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.postgresql.jdbc.PgStatement;

import com.apenasolinco.board_tarefas_avanade_2025.dto.BoardColumnDTO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnEntity;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.CardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardColumnDAO {

	private final Connection connection;

	public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException {
		var sql = "INSERT INTO Boards_Columns (name, order_, kind, board_id) VALUES (?, ?, ?, ?)";

		try (var statement = connection.prepareStatement(sql)) {
			var i = 1;
			statement.setString(i++, entity.getName());
			statement.setInt(i++, entity.getOrder());
			statement.setString(i++, entity.getKind().name());
			statement.setLong(i++, entity.getBoard().getId());
			statement.executeUpdate();

			if (statement instanceof PgStatement pgStatement) {
				entity.setId(pgStatement.getLastOID());
			}

			return entity;
		}
	}

	public List<BoardColumnEntity> findByBoardId(final Long board_id) throws SQLException {
		List<BoardColumnEntity> entities = new ArrayList<>();

		var sql = "SELECT id, name, order_, kind FROM Boards_Columns WHERE board_id = ? ORDER BY order_";

		try (var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, board_id);
			var resultSet = statement.executeQuery();

			while (resultSet.next()) {
				var entity = new BoardColumnEntity();
				entity.setId(resultSet.getLong("id"));
				entity.setName(resultSet.getString("name"));
				entity.setOrder(resultSet.getInt("order_"));
				entity.setKind(BoardColumnKind.valueOf(resultSet.getString("kind")));

				entities.add(entity);
			}

			return entities;
		}

	}

	public List<BoardColumnDTO> findByBoardIdWithDetails(final Long board_id) throws SQLException {
		List<BoardColumnDTO> dtos = new ArrayList<>();
		
		var sql = """
				SELECT
					bc.id,
					bc.name,
					bc.kind,
					COUNT(
						c.id
					) AS cards_amount
				FROM
					Boards_Columns bc
				LEFT JOIN
					Cards c
				ON
					c.board_column_id = bc.id
				WHERE
					board_id = ?
				GROUP BY
					bc.id,
					bc.name,
					bc.order_,
					bc.kind
				""";

		try (var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, board_id);
			var resultSet = statement.executeQuery();

			while (resultSet.next()) {
				// @formatter:off
				var dto = new BoardColumnDTO(
					resultSet.getLong("id"),
					resultSet.getString("name"),
					BoardColumnKind.valueOf(resultSet.getString("kind")),
					resultSet.getInt("cards_amount")
				);
				// @formatter:on

				dtos.add(dto);
			}

			return dtos;
		}

	}

	public Optional<BoardColumnEntity> findById(final Long boardId) throws SQLException {
		// @formatter:off
		var sql =
			"""
			SELECT
				bc.name, 
				bc.kind,
				c.id,
				c.title,
				c.description
			FROM 
				Boards_Columns bc
			INNER JOIN
				Cards c
			ON
				c.board_column_id = bc.id
			WHERE 
				bc.id = ?
			""";
		// @formatter:on

		try (var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, boardId);
			var resultSet = statement.executeQuery();

			if (resultSet.next()) {
				var entity = new BoardColumnEntity();
				entity.setName(resultSet.getString("bc.name"));
				entity.setKind(BoardColumnKind.valueOf(resultSet.getString("bc.kind")));
				
				do {
					var card = new CardEntity();
					card.setId(resultSet.getLong("c.id"));
					card.setTitle(resultSet.getString("c.title"));
					card.setDescription(resultSet.getString("c.description"));
					
					entity.getCards().add(card);
				} while(resultSet.next());

				return Optional.of(entity);
			}

			return Optional.empty();
		}

	}
}






























