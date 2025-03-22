package com.apenasolinco.board_tarefas_avanade_2025.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.BoardColumnDAO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.BoardDAO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardQueryService {
	
	private Connection connection;
	
	public Optional<BoardEntity> findById(final Long id) throws SQLException {
		var boardDao = new BoardDAO(connection);
		var boardColumnDao = new BoardColumnDAO(connection);
		
		var optional = boardDao.findById(id);
		
		if(optional.isPresent()) {
			var entity = optional.get();
			entity.setColumns(boardColumnDao.findByBoardId(entity.getId()));
		}
		
		return Optional.empty();
	}
	
}
