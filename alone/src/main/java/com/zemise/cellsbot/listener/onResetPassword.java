package com.zemise.cellsbot.listener;

import com.zemise.cellsbot.common.util.databaseUtil.MysqlUtil;
import com.zemise.cellsbot.common.util.databaseUtil.ResultSetUtil;
import com.zemise.cellsbot.common.util.databaseUtil.SQLCommandType;
import com.zemise.cellsbot.common.util.databaseUtil.SqlCommandUtil;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.connectMysql;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Author Zemise_
 * @Date 2023/4/16
 * @Description 重置authme密码
 */
public class onResetPassword extends SimpleListenerHost {
    // 1. 监听"重置密码"
    @EventHandler
    public void onReset(GroupMessageEvent event) {
        String keyWord = "重置密码";
        boolean contains = event.getMessage().contentToString().contains(keyWord);
        long senderID = event.getSender().getId();

        if (!contains) return;
        MessageChainBuilder msg = new MessageChainBuilder().append(new At(senderID));

        try {
            resetPassword(senderID);
            msg.append(" 已重置密码，请自行登陆服务器大厅，再度设置密码");
        } catch (SQLException e) {
            msg.append(" 重置密码发生未知错误，请管理大大调试吧，以下是错误信息：\n").append(e.getMessage());
            e.printStackTrace();
        }
        BotOperator.sendGroupMessage(event.getGroup().getId(), msg.build());
    }

    private void resetPassword(Long senderID) throws SQLException {
        MysqlUtil mysql = new connectMysql().getMysql();
        String tableName = "authme";

        String whereClause = String.format("QQ_id = '%s'", senderID);

        // 查询其对应的MC_id
        String sqlCommand = SqlCommandUtil.generateSQLCommand(SQLCommandType.SELECT, "cells_whitelist", "MC_id", null, whereClause);
        System.out.println(sqlCommand);
        ResultSet resultSet = mysql.executeQuery(sqlCommand);

        List<Map<String, Object>> maps = ResultSetUtil.parseResultSet(resultSet);

        String MC_id = "";
        for (Map<String, Object> map : maps) {
            MC_id = map.get("MC_id").toString();
        }

        whereClause = String.format("username = '%s'", MC_id);
        // 生成删除语句
        sqlCommand = SqlCommandUtil.generateDeleteCommand(tableName, whereClause);
        mysql.execute(sqlCommand);

        mysql.closeConnection();
    }

}
