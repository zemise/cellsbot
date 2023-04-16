package com.zemise.cellsbot.common.util.databaseUtil;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;


/**
 * @Author Zemise_
 * @Date 2023/4/8
 * @Description Mysql数据库连接
 */
public class MysqlUtil {
    private final String mysqlHost;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private DataSource dataSource;

    public MysqlUtil(String mysqlHost, int port, String database, String username, String password) {
        this.mysqlHost = mysqlHost;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        initDataSource();
    }

    // 初始化数据源
    private void initDataSource() {
        HikariConfig config = new HikariConfig();
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        // value为字节值，若遇到Packet for query is too large错误，可调小
        // onfig.addDataSourceProperty("maxAllowedPacket", "65510");
        config.setDriverClassName(JDBC_DRIVER);
        config.setJdbcUrl("jdbc:mysql://" + mysqlHost + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        this.dataSource = new HikariDataSource(config);
    }

    /**
     * 执行 SQL 查询并返回 ResultSet 对象
     *
     * @param sqlCommand SQL 查询语句
     * @return ResultSet 查询结果
     */
    public ResultSet executeQuery(String sqlCommand) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlCommand);
    }

    /**
     * 执行 SQL 语句
     *
     * @param sqlCommand SQL 查询语句
     */
    public void execute(String sqlCommand) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(sqlCommand);
    }


    /**
     * 根据表名和字段名查询所有数据
     *
     * @param tableName  表名
     * @param columnName 字段名
     * @return ResultSet 包含查询结果的 ResultSet 对象
     */
    public ResultSet getAllDataByColumnName(String tableName, String columnName) throws SQLException {
        String sqlCommand = "SELECT " + columnName + " FROM " + tableName;
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlCommand);
    }


    /**
     * 执行 SQL 更新操作（如 INSERT、UPDATE、DELETE）并返回更新行数
     *
     * @param sqlCommand SQL 更新语句
     * @return int 更新行数
     */
    public int executeUpdate(String sqlCommand) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sqlCommand);
    }

    /**
     * 执行 SQL 批量更新操作并返回更新行数
     *
     * @param sqlCommands SQL 更新语句数组
     */
    public void executeBatchUpdate(String[] sqlCommands) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        for (String sqlCommand : sqlCommands) {
            statement.addBatch(sqlCommand);
        }
        statement.executeBatch();
    }

    public void executeBatchUpdate(List<String> sqlCommands) throws SQLException {
        String[] sqlCommandArray = sqlCommands.toArray(new String[0]);
        executeBatchUpdate(sqlCommandArray);
    }

    /**
     * 关闭数据库连接
     */
    public void closeConnection() throws SQLException {
        if (dataSource != null && dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }

}
