package com.apenasolinco.board_tarefas_avanade_2025.ui;

import static com.apenasolinco.board_tarefas_avanade_2025.persistence.config.ConnectionConfig.getConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnEntity;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardColumnKind;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.entity.BoardEntity;
import com.apenasolinco.board_tarefas_avanade_2025.service.BoardQueryService;
import com.apenasolinco.board_tarefas_avanade_2025.service.BoardService;

public class MainMenu {

	private final Scanner scanner = new Scanner(System.in);

	public void execute() throws SQLException {
		System.out.println("Bem-vindo ao gerenciador de boards! Escolha a opção desejada:");
		var option = -1;

		while (true) {
			System.out.println("1 - Criar um novo Board");
			System.out.println("2 - Selecionar um Board existente");
			System.out.println("3 - Excluir um board");
			System.out.println("4 - Sair");
			option = scanner.nextInt();

			switch (option) {
				case 1 -> createBoard();
				case 2 -> selectBoard();
				case 3 -> deleteBoard();
				case 4 -> System.exit(0);
				default -> System.out.println("Opção inválida.");
			}
		}
	}

	private void createBoard() throws SQLException {
		var entity = new BoardEntity();

		System.out.println("Informe o nome do Board:");
		entity.setName(scanner.next());

		System.out.println("Informe o número de colunas adicionais do board (0 - n): ");
		var extraCols = scanner.nextInt();

		List<BoardColumnEntity> columns = new ArrayList<>();

		// Coluna inicial
		System.out.println("Informe o nome da coluna inicial do Board: ");
		var initialColName = scanner.next();
		var initialCol = createColumnEntity(initialColName, BoardColumnKind.INITIAL, 0);
		columns.add(initialCol);

		// Colunas extras
		for (int i = 0; i < extraCols; i++) {
			System.out.printf("Informe o nome da coluna de tarefas pendentes núnero %d do Board:\n", i + 1);
			var name = scanner.next();
			var pendingCol = createColumnEntity(name, BoardColumnKind.PENDING, i + 1);
			columns.add(pendingCol);
		}

		// Coluna final
		System.out.println("Informe o nome da coluna final do Board: ");
		var finalColName = scanner.next();
		var finalCol = createColumnEntity(finalColName, BoardColumnKind.FINAL, columns.size());
		columns.add(finalCol);

		// Coluna cancelamento
		System.out.println("Informe o nome da coluna de cancelamento do Board: ");
		var cancelColName = scanner.next();
		var cancelCol = createColumnEntity(cancelColName, BoardColumnKind.FINAL, columns.size() + 1);
		columns.add(cancelCol);

		entity.setColumns(columns);
		try (var connection = getConnection()) {
			var service = new BoardService(connection);
			service.insert(entity);
		}
	}

	private void selectBoard() throws SQLException {
		System.out.println("Informe o id do Board a ser selecionado: ");
		var id = scanner.nextLong();

		try (var connection = getConnection()) {
			var queryService = new BoardQueryService(connection);
			var optional = queryService.findById(id);

			optional.ifPresentOrElse(b -> {
				var boardMenu = new BoardMenu(b);
				boardMenu.execute();
			}, () -> System.out.println("Não foi possível econtrar um board com o id informado."));
		}
	}

	private void deleteBoard() throws SQLException {
		System.out.println("Informe o id do board a ser excluído: ");
		var id = scanner.nextLong();

		try (var connection = getConnection()) {
			var service = new BoardService(connection);

			if (service.delete(id)) {
				System.out.println("Board deletado com sucesso.");
			} else {
				System.out.println("Não foi possível deletar o board. Id inexistente.");
			}
		}
	}

	private BoardColumnEntity createColumnEntity(final String name, final BoardColumnKind kind, final int order) {
		var boardColumn = new BoardColumnEntity();
		boardColumn.setName(name);
		boardColumn.setKind(kind);
		boardColumn.setOrder(order);

		return boardColumn;
	}

}
