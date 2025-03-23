package com.apenasolinco.board_tarefas_avanade_2025.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.BoardColumnDAO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardColumnQueryService {
	
	private final Connection connection;
	
	public Optional<BoardColumnEntity> findById(final Long id) throws SQLException {
		
		var dao = new BoardColumnDAO(connection);
		
		return dao.findById(id);
		
	}
	
}
















