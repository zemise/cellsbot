package com.zemise.cellsbot.common.util.databaseUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Zemise_
 * @Date 2023/4/9
 * @Description 数据库查询结果处理
 */
public class ResultSetUtil {
    /**
     * 解析数据库查询结果集
     *
     * @param resultSet 查询结果集
     * @return 包含查询结果的List集合，每个元素是一个Map，键为列名，值为对应的数据值
     * @throws SQLException 如果解析结果集出现异常，则抛出SQLException
     */
    public static List<Map<String, Object>> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                row.put(columnName, columnValue);
            }
            result.add(row);
        }

        return result;
    }


}
