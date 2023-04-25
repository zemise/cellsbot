package com.zemise.cellsbot.common.util.databaseUtil;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.databaseUtil.MysqlUtil;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Author Zemise_
 * @Date 2023/4/16
 * @Description 连接数据库
 */
public class connectMysql {
    @Getter
    private MysqlUtil mysql;

    public connectMysql() {
        Configuration config = ConfigManager.getConfig();

        String mysqlHost = config.getString("mysql-host");
        int port = config.getInt("mysql-port");
        String database = config.getString("mysql-database");
        String username = config.getString("mysql-username");
        String password = config.getString("mysql-password");

        //连接数据库
        mysql = new MysqlUtil(mysqlHost, port, database, username, password);

    }

    public static String searchMC_id(Long senderID) {
        String MC_id = null;
        try {
            MysqlUtil mysql = new connectMysql().getMysql();

            String whereClause = String.format("QQ_id = '%s'", senderID);
            // 查询其对应的MC_id
            String sqlCommand = SqlCommandUtil.generateSQLCommand(SQLCommandType.SELECT, "cells_whitelist", "MC_id", null, whereClause);

            ResultSet resultSet = mysql.executeQuery(sqlCommand);

            List<Map<String, Object>> maps = ResultSetUtil.parseResultSet(resultSet);


            for (Map<String, Object> map : maps) {
                MC_id = map.get("MC_id").toString();
            }
        } catch (SQLException e) {
            return null;
        }
        return MC_id;
    }
}
