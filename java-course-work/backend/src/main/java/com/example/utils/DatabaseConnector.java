package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

	private static final String URL = "jdbc:postgresql://ioann44.ru:5501/database";
	private static final String USER = "user";
	private static final String PASSWORD = "L7fdfGfh0k9Ck07y81";

	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
