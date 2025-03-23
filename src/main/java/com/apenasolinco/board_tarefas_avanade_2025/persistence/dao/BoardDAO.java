package com.apenasolinco.board_tarefas_avanade_2025.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardDAO {

	private final Connection connection;

	public BoardEntity insert(final BoardEntity entity) throws SQLException {
		var sql = "INSERT INTO Boards (name) VALUES (?) RETURNING id";
		try (var statement = connection.prepareStatement(sql)) {
			statement.setString(1, entity.getName());
			var resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				var id = resultSet.getLong("id");
				entity.setId(id);
			}
			
			return entity;
		}
	}

	public void delete(final Long id) throws SQLException {
		var sql = "DELETE FROM Boards WHERE id = ?";
		try(var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id);
			statement.executeUpdate();
		}
	}

	public Optional<BoardEntity> findById(final Long id) throws SQLException {
		var sql = "SELECT * FROM Boards WHERE id = ?";
		try (var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id);
			statement.executeQuery();

			var resultSet = statement.getResultSet();
			if (resultSet.next()) {
				var entity = new BoardEntity();
				entity.setId(resultSet.getLong("id"));
				entity.setName(resultSet.getString("name"));
				return Optional.of(entity);
			}

			return Optional.empty();
		}
	}

	public boolean exists(final Long id) throws SQLException {
		var sql = "SELECT * FROM Boards WHERE id = ?";
		try (var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id);
			statement.executeQuery();
			return statement.getResultSet().next();
		}
	}

}
