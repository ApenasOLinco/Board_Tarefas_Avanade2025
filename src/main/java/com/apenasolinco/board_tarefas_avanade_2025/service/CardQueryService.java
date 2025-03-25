package com.apenasolinco.board_tarefas_avanade_2025.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.apenasolinco.board_tarefas_avanade_2025.dto.CardDetailsDTO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.dao.CardDAO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CardQueryService {
	
	private Connection connection;
	
	public Optional<CardDetailsDTO> findById(Long id) throws SQLException {
		var dao = new CardDAO(connection);
		return dao.findById(id);
	}
	
}
