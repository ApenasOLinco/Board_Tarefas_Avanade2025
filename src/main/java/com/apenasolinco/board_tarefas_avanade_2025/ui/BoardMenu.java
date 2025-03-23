package com.apenasolinco.board_tarefas_avanade_2025.ui;

import java.util.Scanner;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardMenu {

	private final BoardEntity entity;
	private final Scanner scanner;

	public void execute() {
		System.out.println("----------------------------------------------------");
		System.out.printf("Board %d: %s\nEscolha sua opção:\n", entity.getId(), entity.getName());

		var option = -1;

		while (true) {
			System.out.println("1 - Criar um Card");
			System.out.println("2 - Mover um Card");
			System.out.println("3 - Bloquear um Card");
			System.out.println("4 - Desbloquear um Card");
			System.out.println("5 - Cancelar um Card");
			System.out.println("6 - Visualizar Board");
			System.out.println("7 - Visualizar coluna com Cards");
			System.out.println("8 - Visualizar um Card");
			System.out.println("9 - Voltar ao menu anterior");
			System.out.println("10 - Sair");

			option = Integer.parseInt(scanner.nextLine());

			switch (option) {
				case 1 -> moveCard();
				case 2 -> moveCard();
				case 3 -> unblockCard();
				case 4 -> unblockCard();
				case 5 -> cancelCard();
				case 6 -> showBoard();
				case 7 -> showColumn();
				case 8 -> showCard();
				case 9 -> {
					return;
				}
				case 10 -> System.exit(0);
				default -> System.out.println("Opção inválida.");
			}
		}
	}

	private void moveCard() {
		
	}

	private void unblockCard() {
		
	}

	private void cancelCard() {
		
	}

	private void showBoard() {
		
	}

	private void showColumn() {
		
	}

	private void showCard() {
		
	}

}
