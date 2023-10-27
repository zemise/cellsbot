package com.zemise.cellsbot.waterfall.utils.DatabaseUtil;


import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.evnet.EventHandler;
import com.zemise.cellsbot.common.evnet.Listener;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.event.BotLoginEvent;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.NormalMember;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Slf4j
public class InitTable implements Listener {

    @EventHandler
    public static void afterBotLonging(BotLoginEvent event) {
        /**
         * 初始化或者更新cells_users表，获取成员群的相关信息并插入表
         */
        // 机器人登录成功，执行后续操作
        init_cells();
    }


    public static void init_cells() {
        Configuration config = ConfigManager.getConfig();

        //只有在使用mysql数据库时，才初始化cells_users表
        if (!config.getBoolean("use-mysql")) {
            return;
        }
        PreparedStatement stmt = null;
        //Main.instance.say("正在更新数据库表cells_users...");
        log.info("正在更新数据库表cells_users...");

        try {
            ContactList<NormalMember> members = Objects.requireNonNull(BotOperator.getBot().getGroup(config.getLong("player-group"))).getMembers();
            String sql = "insert into cells_users " +
                    "(QQ_id, QQ_name, QQ_avatar, QQ_Card, QQ_permission, MC_id, join_time, last_say_time) " +
                    "values " +
                    "(?, ?, ?, ?, ?, ?, ?, ?) " +
                    "on duplicate key update QQ_id=values(QQ_id)";

            stmt = new MysqlUtil().getC().prepareStatement(sql);
            for (NormalMember member : members) {
                stmt.setLong(1, member.getId());
                stmt.setString(2, member.getNick());
                stmt.setString(3, member.getAvatarUrl());
                stmt.setString(4, member.getNameCard());
                stmt.setInt(5, member.getPermission().getLevel());
                stmt.setString(6, "未记录");
                stmt.setString(7, "未记录");
                stmt.setString(8, "无本地发言记录");
                stmt.addBatch();
            }

            int[] counts = stmt.executeBatch();
            //Main.instance.say("成功更新 " + counts.length + " 条数据！");
            log.info("成功更新 " + counts.length + " 条数据！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
