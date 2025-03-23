package com.apenasolinco.board_tarefas_avanade_2025.dto;

import java.util.List;

// @formattter:off
public record BoardDetailsDTO(
		Long id,
		String name,
		List<BoardColumnDTO> columns
) {
//@formatter:on
}
