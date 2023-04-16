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
 * @Date 2023/4/9
 * @Description 测试根据luckPerms表更新白名单的玩家uuid
 */
public class testUpdateUUID {
    @TestOnly
    public static void main(String[] args) throws SQLException {
        MysqlUtil mysql
                = new MysqlUtil("localhost", 3306, "cellcraft", "database", "password");

        //查询luckeperms_palyer表中所有数据
        String s1 = SqlCommandUtil.generateSQLCommand(SQLCommandType.SELECT, "luckperms_players", "*", null);
        ResultSet resultSet = mysql.executeQuery(s1);

        List<Map<String, Object>> maps1 = ResultSetUtil.parseResultSet(resultSet);
        String tableName = "cells_whitelist";

        ArrayList<String> sqlList = new ArrayList<>();

        for (Map<String, Object> stringObjectMap : maps1) {
            String columns = "uuid";
            String values = stringObjectMap.get("uuid").toString();
            String whereClause = String.format("MC_id = '%s'", stringObjectMap.get("username"));

            String sqlCommand = SqlCommandUtil.generateSQLCommand(SQLCommandType.UPDATE, tableName, columns, values, whereClause);

            sqlList.add(sqlCommand);
        }
        mysql.executeBatchUpdate(sqlList);
    }
}
