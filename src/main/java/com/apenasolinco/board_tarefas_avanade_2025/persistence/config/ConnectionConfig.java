package com.apenasolinco.board_tarefas_avanade_2025.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ConnectionConfig {
	
	public static Connection getConnection() throws SQLException {
		var environment = System.getenv();
		
		// Build URL
		var dbEnvr = environment.get("DBENV");
		var dbHost = environment.get("DBHOST");
		var dbPort = environment.get("DBPORT");
		var dbName = environment.get("DBNAME");
		var url = "jdbc:%s://%s:%s/%s".formatted(dbEnvr, dbHost, dbPort, dbName);
		
		var user = environment.get("DBUSER");
		var password = environment.get("DBPASSWORD");
		var connection = DriverManager.getConnection(url, user, password);
		connection.setAutoCommit(false);
		
		return connection;
	}
	
}
