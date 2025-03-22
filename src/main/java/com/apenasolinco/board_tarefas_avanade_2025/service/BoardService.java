package com.apenasolinco.board_tarefas_avanade_2025.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.BoardColumnDAO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.BoardDAO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardService {

	private final Connection connection;

	public BoardEntity insert(BoardEntity entity) throws SQLException {
		var dao = new BoardDAO(connection);
		var boardColumnDAO = new BoardColumnDAO(connection);

		try {
			dao.insert(entity);
			var columns = entity.getColumns().stream().map(c -> {
				c.setBoard(entity);
				return c;
			}).toList();

			for (var col : columns) {
				boardColumnDAO.insert(col);
			}
			
			connection.commit();
			return entity;
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		}
	}

	public boolean delete(final Long id) throws SQLException {

		var dao = new BoardDAO(connection);

		try {
			if (!dao.exists(id))
				return false;

			dao.delete(id);
			connection.commit();
			return true;
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		}

	}

}
