package com.apenasolinco.board_tarefas_avanade_2025.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ConnectionConfig {
	
	public static Connection getConnection() throws SQLException {
		var url = "jdbc:postgresql://localhost/board";
		var user = "postgres";
		var password = "postgres";
		var connection = DriverManager.getConnection(url, user, password);
		connection.setAutoCommit(false);
		
		return connection;
	}
	
}
