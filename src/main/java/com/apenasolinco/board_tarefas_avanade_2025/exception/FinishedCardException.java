package com.apenasolinco.board_tarefas_avanade_2025.exception;


/**
 * Thrown when there is an attempt to do an invalid operation to an already finished card
 */
@SuppressWarnings("serial")
public class FinishedCardException extends IllegalStateException {

	public FinishedCardException(String message) {
		super(message);
	}
	
}
