package com.urise.webapp.sql;

import java.sql.CallableStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ISqlExecutorVoid {

	void execute(CallableStatement statement) throws SQLException;
}
