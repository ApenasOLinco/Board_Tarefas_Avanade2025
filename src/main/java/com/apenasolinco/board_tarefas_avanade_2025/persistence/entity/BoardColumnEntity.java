package com.apenasolinco.board_tarefas_avanade_2025.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class BoardColumnEntity {
	
	private Long id;
	private String name;
	private Integer order;
	private BoardColumnKind kind;
	private BoardEntity board = new BoardEntity();
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<CardEntity> cards = new ArrayList<>();
	
}
