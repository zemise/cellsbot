package com.zemise.cellsbot.listener;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.evnet.EventHandler;
import com.zemise.cellsbot.common.evnet.Listener;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.event.BotLoginEvent;
import com.zemise.cellsbot.member.McPlayer;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 监听QQ bot登陆后，完成后续一系列操作
 */
public class onBotLogin implements Listener {

    @EventHandler
    public static void afterBotLonging(BotLoginEvent event) {
        // todo QQ登陆后，尝试进行数据库的连接，获得所有玩家信息，放在数组中，方便查询
        // todo 目前来说，数据库连接还比较麻烦，可以先把设定的玩家群和OP群的玩家信息获取

        Group group = BotOperator.getBot().getGroup(ConfigManager.getConfig().getLong("player-group"));
        ContactList<NormalMember> members = group.getMembers();

        for (NormalMember member : members) {
            McPlayer singlePlayer = new McPlayer(member.getNick(), "all is alex", member.getId());
            System.out.println(singlePlayer.getQqID());
        }
    }
}
