package com.urise.webapp.examples.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.PooledConnection;

import oracle.jdbc.pool.OraclePooledConnection;

public class MainPooledConnection {

	public static void main(String[] args) throws SQLException {
		PooledConnection pooledConnection = new OraclePooledConnection("jdbc:oracle:thin:@krr-sql23:1521:develop",
				"DEV", "START123");
		try (Connection connection = pooledConnection.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT ROWID, T.* FROM DICT_GOODS T ORDER BY 2")) {
			while (resultSet.next()) {
				System.out.printf("%18s %2d %14s\n", resultSet.getRowId("ROWID"),
													 resultSet.getInt("ID_GOODS"),
													 resultSet.getString("NAME_GOODS"));
			}
		}
	}
}
