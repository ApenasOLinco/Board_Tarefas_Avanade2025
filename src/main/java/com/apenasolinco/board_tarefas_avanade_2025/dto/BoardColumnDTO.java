package com.apenasolinco.board_tarefas_avanade_2025.dto;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind;

// @formatter:off
public record BoardColumnDTO(
		Long id,
		String name, 
		BoardColumnKind kind, 
		int cardsAmount
) {
// @formatter:on
	
	
}
