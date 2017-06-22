package com.urise.webapp.sql;

import java.sql.CallableStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ISqlExecutor<T> {

	T execute(CallableStatement statement) throws SQLException;
}
