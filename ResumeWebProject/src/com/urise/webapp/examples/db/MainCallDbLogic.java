package com.urise.webapp.examples.db;

import java.sql.JDBCType;

import com.urise.webapp.sql.ISqlExecutor;
import com.urise.webapp.sql.ISqlExecutorVoid;
import com.urise.webapp.sql.SqlHelper;

import oracle.jdbc.OracleType;

public class MainCallDbLogic {

	public static void main(String[] args) {
		callFunction();
		callProcedure();
	}

	public static void callFunction() {
		ISqlExecutor<Integer> sqlExecutor = statement -> {
			statement.setInt(1, 3);
			statement.setInt(2, 7);
			statement.registerOutParameter(3, JDBCType.INTEGER);

			statement.execute();
			return statement.getInt(3);
		};
		System.out.println(SqlHelper.getResult("call getSum(?,?) into ?", sqlExecutor, "error in method getSum !"));
	}

	public static void callProcedure() {
		ISqlExecutorVoid sqlExecutor = statement -> {
			statement.setInt(1, 5);
			statement.setInt(2, 7);
			statement.registerOutParameter(3, JDBCType.INTEGER);
			statement.registerOutParameter(4, OracleType.VARCHAR2);

			statement.execute();
			System.out.println(statement.getString(4) + " = " + statement.getInt(3));
		};
		SqlHelper.executeSql("call pro(?,?,?,?)", sqlExecutor, "error in procedure pro !");
	}
}
/*
create or replace function getSum(n1 integer, n2 integer) return integer is
begin
  return n1 + n2;
end getSum;

SqlPlus
------------------
var result number;
call getSum(&num1, &num2) into :result;

create or replace procedure pro(n1      IN  integer, 
                                n2      IN  integer, 
                                result1 OUT integer, 
                                result2 OUT varchar2)  is
begin
    result1 :=n1 + n2;
    result2 :=n1||' + '||n2;
end pro;

SqlPlus
--------------
var r1 number;
var r2 VarChar2(100);
call pro(&num1, &num2, :r1, :r2);

10
5 + 7 = 12
*/
