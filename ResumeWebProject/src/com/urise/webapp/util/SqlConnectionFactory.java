package com.urise.webapp.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.postgresql.ds.PGConnectionPoolDataSource;

public class SqlConnectionFactory {

	private static SqlConnectionFactory instance;

	private final PGConnectionPoolDataSource poolDataSource;

	private SqlConnectionFactory() {
		Config config = Config.getInstance();
		poolDataSource = new PGConnectionPoolDataSource();
		poolDataSource.setServerName(config.getDbServerName());
		poolDataSource.setDatabaseName(config.getDatabaseName());
		poolDataSource.setPortNumber(config.getDbPortNumber());
		poolDataSource.setUser(config.getDbUser());
		poolDataSource.setPassword(config.getDbPassword());
		poolDataSource.setSsl(config.getSsl());
		poolDataSource.setSslfactory(config.getSslFactory());
	}

	public static SqlConnectionFactory getInstance() {
		if (Objects.isNull(instance)) {
			instance = new SqlConnectionFactory();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		Connection connection = poolDataSource.getConnection();
		connection.setAutoCommit(false);
		return connection;
	};
}
