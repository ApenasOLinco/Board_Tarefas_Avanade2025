package com.apenasolinco.board_tarefas_avanade_2025.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {
	
	private Long id;
	private String name;
	private Integer order;
	private BoardColumnKind kind;
	private BoardEntity board;
	
}
