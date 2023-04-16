package com.zemise.cellsbot;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.databaseUtil.MysqlUtil;
import lombok.Getter;

/**
 * @Author Zemise_
 * @Date 2023/4/16
 * @Description
 */
public class connectMysql {
    @Getter
    private MysqlUtil mysql;

    public void connetMysql() {
        Configuration config = ConfigManager.getConfig();

        String mysqlHost = config.getString("mysql-host");
        int port = config.getInt("mysql-port");
        String database = config.getString("mysql-database");
        String username = config.getString("mysql-username");
        String password = config.getString("mysql-password");

        //连接数据库
        mysql = new MysqlUtil(mysqlHost, port, database, username, password);

    }
}
