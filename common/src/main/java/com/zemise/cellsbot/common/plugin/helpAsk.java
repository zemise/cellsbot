package com.zemise.cellsbot.common.plugin;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.databaseUtil.*;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.MiraiMessageUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Author zemise_
 * @Date 2023/4/23
 * @Description 群诉求消息处理
 */
public class helpAsk {
    public static void please(GroupMessageEvent e, Long groupId, Long senderID, MessageChain helpMsg) {
        Configuration config = ConfigManager.getConfig();
        String MC_id = connectMysql.searchMC_id(senderID);

        if (MC_id == null) {
            BotOperator.sendGroupMessage(groupId, MiraiMessageUtil.quoteReply(e, "未绑定ID，无法使用此功能"));
            return;
        }

        BotOperator.sendGroupMessage(groupId, new MessageChainBuilder().
                append(new At(senderID)).
                append("\n你的诉求已转发至管理群，热心的管理员将尽快解决\n").
                append("\n转发诉求内容如下：\n").
                append(helpMsg).
                build());

        BotOperator.sendGroupMessage(config.getLongList("op-group"), new MessageChainBuilder()
                .append(AtAll.INSTANCE)  //这里要注意，如果bot不是群管理员身份，这条消息发不出来
                .append("\n玩家群有人寻求管理帮助啦：\n")
                .append(MC_id)
                .append(" (qq:")
                .append(senderID.toString())
                .append(")：\n")
                .append(helpMsg)
                .build());
    }
}
