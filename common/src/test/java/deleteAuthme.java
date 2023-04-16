import com.zemise.cellsbot.common.util.databaseUtil.MysqlUtil;
import com.zemise.cellsbot.common.util.databaseUtil.ResultSetUtil;
import com.zemise.cellsbot.common.util.databaseUtil.SQLCommandType;
import com.zemise.cellsbot.common.util.databaseUtil.SqlCommandUtil;
import org.jetbrains.annotations.TestOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Zemise_
 * @Date 2023/4/16
 * @Description 测试删除数据库中的某一列
 */
public class deleteAuthme {
    @TestOnly
    public static void main(String[] args) throws SQLException {
        MysqlUtil mysql
                = new MysqlUtil("hostIp", 3306, "database", "root", "password");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("MC_id");
        strings.add("QQ_id");

        String sqlCommand = SqlCommandUtil.generateSQLCommand(SQLCommandType.SELECT, "cells_whitelist", strings, null, "QQ_id = 123456");
        System.out.println(sqlCommand);
        ResultSet resultSet = mysql.executeQuery(sqlCommand);

        List<Map<String, Object>> maps1 = ResultSetUtil.parseResultSet(resultSet);
        System.out.println(maps1);

        String tableName = "authme";
        //String column =
        String sqlCommand1 = SqlCommandUtil.generateDeleteCommand("authme", "QQ_id = 99999");
        System.out.println(sqlCommand1);

    }
}