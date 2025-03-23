package com.apenasolinco.board_tarefas_avanade_2025;

import java.sql.SQLException;

import com.apenasolinco.board_tarefas_avanade_2025.persistence.config.ConnectionConfig;
import com.apenasolinco.board_tarefas_avanade_2025.persistence.migration.MigrationStrategy;
import com.apenasolinco.board_tarefas_avanade_2025.ui.MainMenu;

public class Application {
	
	public static void main(String[] args) throws SQLException {
		try (var connection = ConnectionConfig.getConnection()) {
			new MigrationStrategy(connection).executeMigration();;
		}
		
		new MainMenu().execute();
	}
}