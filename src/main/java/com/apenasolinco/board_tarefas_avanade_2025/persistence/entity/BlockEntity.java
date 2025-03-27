package com.apenasolinco.board_tarefas_avanade_2025.persistence.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BlockEntity {
	
	private Long id;
	private String title;
	private LocalDateTime createdAt;
	private String createdReason;
	private LocalDateTime closedAt;
	private String closedReason;
	private CardEntity card = new CardEntity();

}