package com.zemise.cellsbot.common.util.databaseUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Zemise_
 * @Date 2023/4/8
 * @Description Sql基本语句生成
 */
public class SqlCommandUtil {
    private static final int DEFAULT_STRING_BUILDER_CAPACITY = 128;

    /**
     * 生成SQL命令字符串
     *
     * @param commandType SQL命令类型
     * @param tableName   表名
     * @param columns     列名列表
     * @param values      列值列表
     * @param whereClause WHERE子句
     * @return SQL命令字符串
     */
    public static String generateSQLCommand(SQLCommandType commandType, String tableName, List<String> columns, List<String> values, String whereClause) {
        Objects.requireNonNull(commandType, "Command type cannot be null");
        StringBuilder sql = new StringBuilder(DEFAULT_STRING_BUILDER_CAPACITY);

        whereClause = (whereClause == null) ? "" : whereClause;

        switch (commandType) {
            case SELECT:
                // SELECT command
                sql.append("SELECT ")
                        .append(columns == null || columns.isEmpty() ? "*" : String.join(", ", columns))
                        .append(" FROM ")
                        .append(tableName);
                if (!whereClause.isEmpty()) {
                    sql.append(" WHERE ").append(whereClause);
                }
                break;
            case INSERT:
                // INSERT command
                sql.append("INSERT INTO ")
                        .append(tableName)
                        .append(" (")
                        .append(String.join(", ", columns))
                        .append(") VALUES (")
                        .append(String.join(", ", Collections.nCopies(values.size(), "?")))
                        .append(")");
                break;
            case UPDATE:
                // UPDATE command
                String setClause = columns.stream()
                        .map(column -> String.format("%s = '%s'", column, values.get(columns.indexOf(column))))
                        .collect(Collectors.joining(", "));
                sql.append(String.format("UPDATE %s SET %s", tableName, setClause));
                if (!whereClause.isEmpty()) {
                    sql.append(" WHERE ").append(whereClause);
                }
                break;
            case DELETE:
                // DELETE command
                sql.append("DELETE FROM ").append(tableName);
                if (!whereClause.isEmpty()) {
                    sql.append(" WHERE ").append(whereClause);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid SQL command type");
        }

        return sql.toString();
    }

    public static String generateSQLCommand(SQLCommandType commandType, String tableName, String column, String value, String whereClause) {
        List<String> columns = Collections.singletonList(column);
        List<String> values = Collections.singletonList(value);
        return generateSQLCommand(commandType, tableName, columns, values, whereClause);
    }

    public static String generateSQLCommand(SQLCommandType commandType, String tableName, String column, String value) {
        return generateSQLCommand(commandType, tableName, column, value, null);
    }

    /**
     * 生成 DELETE 命令的 SQL 语句。
     *
     * @param tableName   表名。
     * @param whereClause WHERE 子句。
     * @return 生成的 SQL 命令。
     */
    public static String generateDeleteCommand(String tableName, String whereClause) {
        return generateSQLCommand(SQLCommandType.DELETE, tableName, "", "", whereClause);
    }

}