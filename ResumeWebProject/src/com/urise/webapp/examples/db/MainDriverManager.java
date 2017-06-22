package com.urise.webapp.examples.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleDriver;

public class MainDriverManager {

	public static void main(String[] args) throws SQLException {
		DriverManager.registerDriver(new OracleDriver());

		try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@krr-sql23:1521:develop", "DEV",	"START123");
			   Statement statement = connection.createStatement();
			   ResultSet resultSet = statement.executeQuery("SELECT ROWID, T.* FROM DEPT T ORDER BY 2")) {
			while (resultSet.next()) {
				System.out.printf("%18s %2d %10s %8s\n", resultSet.getRowId("ROWID"),
														 resultSet.getInt("DEPTNO"),
														 resultSet.getString("DNAME"), 
														 resultSet.getString("LOC"));
			}
		}
	}
}
