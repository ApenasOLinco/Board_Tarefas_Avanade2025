package com.apenasolinco.board_tarefas_avanade_2025.exception;

import java.util.NoSuchElementException;

@SuppressWarnings("serial")
public class EntityNotFoundException extends NoSuchElementException {

	public EntityNotFoundException(String message) {
		super(message);
	}

}
