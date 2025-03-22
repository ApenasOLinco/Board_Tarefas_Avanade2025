package com.apenasolinco.board_tarefas_avanade_2025.persistence.entity;

import lombok.Data;

@Data
public class CardEntity {
	
	private Long id;
	private String title;
	private String description;
	private Integer order;
	private BoardColumnEntity column;
	
}
