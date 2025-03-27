package com.apenasolinco.board_tarefas_avanade_2025.ui;

import java.sql.SQLException;
import java.util.Scanner;

import com.apenasolinco.board_tarefas_avanade_2025.dto.BoardColumnInfoDTO;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.config.ConnectionConfig;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnEntity;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardEntity;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.CardEntity;
import com.apenasolinco.board_tarefas_avanade_2025.service.BoardColumnQueryService;
import com.apenasolinco.board_tarefas_avanade_2025.service.BoardQueryService;
import com.apenasolinco.board_tarefas_avanade_2025.service.CardQueryService;
import com.apenasolinco.board_tarefas_avanade_2025.service.CardService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardMenu {

	private final BoardEntity entity;
	private final Scanner scanner;

	public void execute() {
		System.out.println("----------------------------------------------------");
		System.out.printf("Board %d: %s\nEscolha sua opção:\n", entity.getId(), entity.getName());

		var option = -1;

		try {
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
					case 1 -> createCard();
					case 2 -> moveCard();
					case 3 -> blockCard();
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void createCard() throws SQLException {
		var card = new CardEntity();

		System.out.println("Insira o título do card a ser criado:");
		card.setTitle(scanner.nextLine());

		System.out.println("Insira a descrição do card a ser criado:");
		card.setDescription(scanner.nextLine());

		// @formatter:off
		var initialColumn = 
			entity.getColumns().stream()
				.filter(
					t -> t.getKind().equals(BoardColumnKind.INITIAL)
				)
				.findFirst()
			.orElseThrow();
		// @formatter:on
		card.setColumn(initialColumn);

		try (var connection = ConnectionConfig.getConnection()) {
			var service = new CardService(connection);
			service.insert(card);
			System.out.println("Card criado com o id " + card.getId());
		}
	}

	private void moveCard() throws SQLException {
		System.out.println("Insira o id do card a ser movido para a próxima coluna:");
		var cardId = Long.parseLong(scanner.nextLine());

		var boardColumnsInfo = entity.getColumns()
				.stream()
				.map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
				.toList();

		try (var connection = ConnectionConfig.getConnection()) {
			var service = new CardService(connection);
			service.moveToNextColumn(cardId, boardColumnsInfo);
			System.out.println("Card de id %s movido!".formatted(cardId));
		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
		}
	}

	private void blockCard() throws SQLException {

		System.out.println("Informe o id do card a ser bloqueado:");
		var cardId = Long.parseLong(scanner.nextLine());

		System.out.println("Informe o motivo do bloqueio do card:");
		var reason = scanner.nextLine();

		try (var connection = ConnectionConfig.getConnection()) {
			var cardService = new CardService(connection);
			var columnsInfo = entity.getColumns()
					.stream()
					.map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
					.toList();
			
			cardService.block(cardId, reason, columnsInfo);
			System.out.println("Card de id %s bloqueado!".formatted(cardId));
		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
		}

	}

	private void unblockCard() throws SQLException {
		System.out.println("Informe o id do card a ser desbloqueado:");
		var cardId = Long.parseLong(scanner.nextLine());
		
		System.out.println("Informe o motivo do desbloqueio do card:");
		var reason = scanner.nextLine();
		
		try (var connection = ConnectionConfig.getConnection()) {
			var cardService = new CardService(connection);
			
			cardService.unblock(cardId, reason);
			System.out.println("Card de id %s desbloqueado!".formatted(cardId));
		} catch(RuntimeException ex) {
			System.err.println(ex.getMessage());
		}
	}

	private void cancelCard() throws SQLException {
		System.out.println("Informe o id do card a ser movido para a coluna de cancelamento:");
		var cardId = Long.parseLong(scanner.nextLine());
		var cancelColumn = entity.getCancelColumn();

		var boardColumnsInfo = entity.getColumns()
				.stream()
				.map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
				.toList();

		try {
			var service = new CardService(ConnectionConfig.getConnection());
			service.cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
			System.out.println("Card de id %s cancelado!".formatted(cardId));
		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
		}
	}

	private void showBoard() throws SQLException {
		try (var connection = ConnectionConfig.getConnection()) {
			var boardDetailsOptional = new BoardQueryService(connection).showBoardDetails(entity.getId());

			boardDetailsOptional.ifPresent(b -> {
				System.out.printf("Board [%s, %s]\n", b.id(), b.name());
				b.columns().forEach(c -> {
					System.out.printf("Coluna [%s]; Tipo: [%s]; Tem: %s card(s)\n", c.name(), c.kind(),
							c.cardsAmount());
				});
			});
		}
	}

	private void showColumn() throws SQLException {
		var columnsIds = entity.getColumns().stream().map(BoardColumnEntity::getId).toList();

		var selectedColumn = -1L;
		while (!columnsIds.contains(selectedColumn)) {
			System.out.printf("Escolha uma coluna do board %s\n", entity.getName());
			entity.getColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));

			selectedColumn = Long.parseLong(scanner.nextLine());
		}

		try (var connection = ConnectionConfig.getConnection()) {
			var service = new BoardColumnQueryService(connection);
			var optional = service.findById(selectedColumn);

			optional.ifPresentOrElse(col -> {
				System.out.printf("Coluna %s; Tipo: %s;\n", col.getName(), col.getKind());
				col.getCards().forEach(card -> {
					System.out.printf("Card %s: %s\nDescrição: %s\n", card.getId(), card.getTitle(),
							card.getDescription());
				});
			}, () -> System.out.println("Essa coluna não tem nenhum card."));
		}
	}

	private void showCard() throws SQLException {
		System.out.println("Informe o id do card que deseja visualizar:");
		var selectedId = Long.parseLong(scanner.nextLine());

		try (var connection = ConnectionConfig.getConnection()) {
			new CardQueryService(connection).findById(selectedId).ifPresentOrElse(c -> {
				System.out.printf("- - - - - - - - - -\n");
				System.out.printf("Card %s - %s\n", c.id(), c.title());
				System.out.printf("Descrição: %s\n", c.description());
				System.out.printf(c.blocked() ? "Está bloqueado. Motivo: %s.\n" : "Não está bloqueado.\n",
						c.blockReason());
				System.out.printf("Bloqueado %s vezes\n", c.blocksAmount());
				System.out.printf("Está na coluna %s - %s\n", c.columnId(), c.columnName());
				System.out.printf("- - - - - - - - - -\n");
			}, () -> {
				System.out.println("Não existe um card com o id " + selectedId);
			});
		}
	}

}
