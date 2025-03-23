package com.apenasolinco.board_tarefas_avanade_2025.persistence.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.jdbc.PgStatement;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnEntity;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardColumnDAO {
	
	private final Connection connection;
	
	public BoardColumnEntity insert(final BoardColumnEntity entity) throws SQLException {
		var sql = "INSERT INTO Boards_Columns (name, order_, kind, board_id) VALUES (?, ?, ?, ?)";
		
		try (var statement = connection.prepareStatement(sql)){
			var i = 1;
			statement.setString(i++, entity.getName());
			statement.setInt(i++, entity.getOrder());
			statement.setString(i++, entity.getKind().name());
			statement.setLong(i++, entity.getBoard().getId());
			statement.executeUpdate();
			
			if(statement instanceof PgStatement pgStatement) {
				entity.setId(pgStatement.getLastOID());
			}
			
			return entity;
		}
	}

	public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException {
		List<BoardColumnEntity> entities = new ArrayList<>();
		
		var sql = "SELECT id, name, order_, kind FROM Boards_Columns WHERE board_id = ? ORDER BY order_";
		
		try(var statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id);
			var resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
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
	
}






















