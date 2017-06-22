package com.urise.webapp.examples.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.urise.webapp.util.DateUtil;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;

public class MainDataSource {

	public static void main(String[] args) throws SQLException {
		OracleDataSource poolDataSource = new OracleConnectionPoolDataSource();
		poolDataSource.setDriverType("thin");
		poolDataSource.setServerName("krr-sql23");
		poolDataSource.setDatabaseName("DEVELOP");
		poolDataSource.setPortNumber(1521);
		poolDataSource.setUser("DEV");
		poolDataSource.setPassword("START123");

		try (Connection connection = poolDataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT ROWID, T.* FROM EMP T WHERE DEPTNO=? ORDER BY ENAME")) {
			statement.setInt(1, 10);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					System.out.printf("%18s %4d %6s %10s %4d %20s %8.2f %8.2f %2d\n",
							resultSet.getRowId("ROWID"),
							resultSet.getInt("EMPNO"), 
							resultSet.getString("ENAME"), 
							resultSet.getString("JOB"),
							resultSet.getInt("MGR"),
							resultSet.getTimestamp("HIREDATE").toLocalDateTime().format(DateUtil.PATTERN_4),
							resultSet.getFloat("SAL"), resultSet.getFloat("COMM"), 
							resultSet.getInt("DEPTNO"));
				}
			}
		}
	}
}
