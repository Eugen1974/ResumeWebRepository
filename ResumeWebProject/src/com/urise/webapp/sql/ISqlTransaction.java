package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ISqlTransaction {

	void execute(Connection connection) throws SQLException;
}
