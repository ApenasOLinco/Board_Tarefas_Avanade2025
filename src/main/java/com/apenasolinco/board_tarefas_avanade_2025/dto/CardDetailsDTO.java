package com.apenasolinco.board_tarefas_avanade_2025.dto;

import java.time.LocalDateTime;

// @formatter:off
public record CardDetailsDTO(
		Long id,
		String title,
		String description,
		boolean blocked, 
		LocalDateTime blockedAt,
		String blockReason, 
		int blocksAmount, 
		Long columnId, 
		String columnName
) {

}
