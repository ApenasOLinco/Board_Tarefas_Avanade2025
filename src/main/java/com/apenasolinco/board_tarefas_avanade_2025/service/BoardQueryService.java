package com.apenasolinco.board_tarefas_avanade_2025.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.apenasolinco.board_tarefas_avanade_2025.dto.BoardDetailsDTO;
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
			
			return Optional.of(entity);
		}
		
		return Optional.empty();
	}
	
	
	public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException {
		var boardDao = new BoardDAO(connection);
		var boardColumnDao = new BoardColumnDAO(connection);
		
		var optional = boardDao.findById(id);
		
		if(optional.isPresent()) {
			var entity = optional.get();
			var columns = boardColumnDao.findByBoardIdWithDetails(entity.getId());
			
			var dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
			return Optional.of(dto);
		}
		
		return Optional.empty();
		
		
	}
	
}
































