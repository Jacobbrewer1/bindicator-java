package com.github.jacobbrewer1.dataaccess;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class MySqlDal {

    protected static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    private final MysqlDataSource mysqlDataSource;

    protected MySqlDal(String schema, String host, String port, String username, String password) throws SQLException {
        mysqlDataSource = new MysqlDataSource();

        mysqlDataSource.setServerName(host);
        mysqlDataSource.setPortNumber(Integer.parseInt(port));
        mysqlDataSource.setDatabaseName(schema);

        mysqlDataSource.setUser(username);
        mysqlDataSource.setPassword(password);

        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setAllowPublicKeyRetrieval(true);
    }

    protected Connection getConnection() throws SQLException {
        return mysqlDataSource.getConnection();
    }
}
