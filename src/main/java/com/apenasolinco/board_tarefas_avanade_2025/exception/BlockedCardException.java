package com.apenasolinco.board_tarefas_avanade_2025.exception;

/**
 * Thrown when there is an attempt to do an invalid operation in a blocked card
 */
@SuppressWarnings("serial")
public class BlockedCardException extends RuntimeException {
	
	public BlockedCardException(String message) {
		super(message);
	}

}
