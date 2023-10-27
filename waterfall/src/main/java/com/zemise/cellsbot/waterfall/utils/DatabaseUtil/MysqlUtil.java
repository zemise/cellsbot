package com.zemise.cellsbot.waterfall.utils.DatabaseUtil;


import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.waterfall.Main;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MysqlUtil {
    @Getter
    private final Connection c = connect();

    /**
     * 说明：建立连接，返回一个Connection实例
     *
     * @return Connection
     */
    private Connection connect() {
        Configuration config = ConfigManager.getConfig();
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String MYSQL_HOST = config.getString("mysql-host");
        String PORT = config.getString("mysql-port");
        String DB_NAME = config.getString("mysql-database");
        String DB_URL = "jdbc:mysql://" + MYSQL_HOST + ":" + PORT + "/" + DB_NAME;

        String USERNAME = config.getString("mysql-username");
        String PASSWORD = config.getString("mysql-password");


        Connection conn = null;
        //如果使用mysql数据库
        if (config.getBoolean("use-mysql")) {
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException e) {
                log.warn("连接mysql驱动失败", e);
            } catch (SQLException e) {
                log.warn("mysql数据库连接失败，请检查配置文件是否正确", e);
            }
        } else {
            //如果使用sqlite数据库
            String url = "jdbc:sqlite:" + new File(Main.getInstance().getDataFolder(), "database.db").getPath();
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(url);
            } catch (ClassNotFoundException e) {
                log.warn("连接sqlite驱动失败", e);
            } catch (SQLException e) {
                log.warn("sqlite数据库连接失败，请检查数据库是否有问题", e);
            }
        }
        return conn;
    }

    /**
     * 说明：初始化数据库，并创建玩家表、白名单表、黑名单表
     */
    public void init() {
        Configuration config = ConfigManager.getConfig();

        try {
            //如果使用mysql数据库
            if (config.getBoolean("use-mysql")) {
                PreparedStatement ps = c.prepareStatement(
                        "create table if not exists cells_users (" +
                                "QQ_id varchar(255) not null," +
                                "QQ_name text not null," +
                                "QQ_avatar text," +
                                "QQ_card text," +
                                "QQ_permission int(5) not null," +
                                "MC_id text," +
                                "Join_time text not null," +
                                "Last_say_time text not null," +
                                "Penalty_points varchar(255)," +
                                "Bonus_points varchar(255)," +
                                "Data json," +
                                "primary key (QQ_id)" +
                                ")ENGINE = INNODB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();

                ps = c.prepareStatement(
                        "create table if not exists cells_whitelist (" +
                                "QQ_id varchar(255) not null," +
                                "QQ_name text not null," +
                                "MC_id text not null," +
                                "uuid text," +
                                "bind_time text not null," +
                                "Data json," +
                                "primary key (QQ_id)" +
                                ")ENGINE = INNODB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();

                ps = c.prepareStatement(
                        "create table if not exists cells_blacklist (" +
                                "QQ_id varchar(255) not null," +
                                "MC_id text not null," +
                                "created text not null," +
                                "sources text not null," +
                                "expires text not null," +
                                "reason text," +
                                "Data json," +
                                "primary key (QQ_id)" +
                                ")ENGINE = INNODB DEFAULT CHARSET=utf8mb4;"
                );
                ps.execute();
                ps.close();
            } else {
                //如果使用sqlite数据库
                PreparedStatement ps = c.prepareStatement(
                        "create table if not exists cells_users (" +
                                "QQ_id varchar(255) not null," +
                                "QQ_name text not null," +
                                "QQ_avatar text," +
                                "QQ_card text," +
                                "QQ_permission int(5) not null," +
                                "MC_id text," +
                                "Join_time text not null," +
                                "Last_say_time text not null," +
                                "Penalty_points varchar(255)," +
                                "Bonus_points varchar(255)," +
                                "Data text," +
                                "primary key (QQ_id)" +
                                ");"
                );
                ps.execute();

                ps = c.prepareStatement(
                        "create table if not exists cells_whitelist (" +
                                "QQ_id varchar(255) not null," +
                                "QQ_name text not null," +
                                "MC_id text not null," +
                                "uuid text," +
                                "bind_time text not null," +
                                "Data text," +
                                "primary key (QQ_id)" +
                                ");"
                );
                ps.execute();

                ps = c.prepareStatement(
                        "create table if not exists cells_blacklist (" +
                                "QQ_id varchar(255) not null," +
                                "MC_id text," +
                                "created text not null," +
                                "sources text  not null," +
                                "expires text not null," +
                                "reason text," +
                                "Data text," +
                                "primary key (QQ_id)" +
                                ");"
                );
                ps.execute();

                ps.close();
            }
        } catch (Exception e) {
            log.warn("数据库创建失败", e);
            e.printStackTrace();
        }
    }

    //给定参数，查询数据库，先只考虑查询QQ账号或这MC的ID

    /**
     * 查寻各数据表中的玩家数据
     *
     * @param arg       QQ账号或者MC_id
     * @param tableName 数据库表名:cells_user，cells_whitelist，cells_blacklist
     * @return 对应表中相应信息的数据集合
     */
    public Map<String, Object> queryPlayer(Object arg, String tableName) {
        String QQ_id = arg.toString();
        String MC_id = arg.toString();


        HashMap<String, Object> queryRes = new HashMap<>();
//        String stringID = "-";
//        Long longID = -1L;

//        //先判定arg具体是String还是Long
//        if (arg instanceof String) {
//            stringID = (String) arg;
//        } else if (arg instanceof Long) {
//            longID = (Long) arg;
//        }

        try {
            Statement statement = getC().createStatement();
            String sql = String.format("select * from %s where QQ_id='%s' or MC_id='%s'", tableName, QQ_id, MC_id);
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    queryRes.put(metaData.getColumnName(i), resultSet.getObject(metaData.getColumnName(i)));
                }

            }
            return queryRes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryRes;
    }

    public void executeCommand(String sqlCommand) throws SQLException {
        try (Statement statement = c.createStatement()) {
            statement.execute(sqlCommand);
        }
    }
}
