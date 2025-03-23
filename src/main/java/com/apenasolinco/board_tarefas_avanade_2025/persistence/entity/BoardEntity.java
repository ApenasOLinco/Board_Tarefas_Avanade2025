package com.apenasolinco.board_tarefas_avanade_2025.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class BoardEntity {

	private Long id;
	private String name;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<BoardColumnEntity> columns = new ArrayList<>();
	
}






















