package com.zemise.cellsbot.event.miari;

import com.zemise.cellsbot.common.util.databaseUtil.MysqlUtil;
import com.zemise.cellsbot.common.util.databaseUtil.SqlCommandUtil;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.databaseUtil.connectMysql;
import com.zemise.cellsbot.common.util.miraiUtil.MiraiMessageUtil;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.sql.SQLException;

/**
 * @Author Zemise_
 * @Date 2023/4/16
 * @Description 重置authme密码
 */
public class ResetPasswordEventHandle extends SimpleListenerHost {
    // 1. 监听"重置密码"
    @EventHandler
    public void onReset(GroupMessageEvent event) {
        String keyWord = "重置密码";
        boolean contains = event.getMessage().contentToString().contains(keyWord);
        long senderID = event.getSender().getId();

        if (!contains) return;
        StringBuilder msg = new StringBuilder();

        try {
            resetPassword(senderID);
            msg.append(" 已重置密码，请自行登陆服务器大厅，再度设置密码");
        } catch (SQLException e) {
            msg.append(" 重置密码发生未知错误，请管理大大调试吧，以下是错误信息：\n").append(e.getMessage());
            e.printStackTrace();
        }
        BotOperator.sendGroupMessage(event.getGroup().getId(), MiraiMessageUtil.quoteReply(event, msg.toString()));
    }

    private void resetPassword(Long senderID) throws SQLException {
        MysqlUtil mysql = new connectMysql().getMysql();
        // 查询其对应的MC_id
        String MC_id = connectMysql.searchMC_id(senderID);

        String whereClause = String.format("username = '%s'", MC_id);
        // 生成删除语句
        String sqlCommand = SqlCommandUtil.generateDeleteCommand("authme", whereClause);
        mysql.execute(sqlCommand);

        mysql.closeConnection();
    }

}
