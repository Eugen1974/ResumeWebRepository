package com.urise.webapp.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.util.SqlConnectionFactory;

public class SqlHelper {

	public static void executeSql(String sql, ISqlExecutorVoid sqlExecutor, String errorMessage) {
		try (Connection connection = SqlConnectionFactory.getInstance().getConnection();
				CallableStatement statement = connection.prepareCall(sql)) {
			sqlExecutor.execute(statement);
		} catch (SQLException e) {
			throw new StorageException(errorMessage + " " + e.toString(), e);
		}
	}

	public static <T> T getResult(String sql, ISqlExecutor<T> sqlExecutor, String errorMessage) {
		try (Connection connection = SqlConnectionFactory.getInstance().getConnection();
				CallableStatement statement = connection.prepareCall(sql)) {
			return sqlExecutor.execute(statement);
		} catch (SQLException e) {
			throw new StorageException(errorMessage + " " + e.toString(), e);
		}
	}

	public static void executeTransaction(ISqlTransaction sqlTransaction, String errorMessage) {
		try (Connection connection = SqlConnectionFactory.getInstance().getConnection()) {
			try {
				sqlTransaction.execute(connection);
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new StorageException(errorMessage + " " + e.toString(), e);
		}
	}
}
