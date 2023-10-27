package com.zemise.cellsbot.waterfall.plugins;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.*;
import com.zemise.cellsbot.waterfall.Main;
import com.zemise.cellsbot.waterfall.McUtil.judge;
import com.zemise.cellsbot.waterfall.utils.DatabaseUtil.MysqlUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.data.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class BindQQandID implements mainPlugin {

    Bot bot = BotOperator.getBot();

    public MessageChain bind(Long groupID, Long senderID, Message[] args, @Nullable QuoteReply quoteReply) throws Exception {
        Configuration config = ConfigManager.getConfig();

        //绑定功能只在玩家群/管理群里启用
        if (groupID != config.getLong("player-group") & groupID != config.getLong("op-group")) {
            throw new GroupNotAllowedException();
        }

        //指令长度符合要求
        if (args.length < 1) {
            throw new InvalidSyntaxException();
        }

        //不满足条件的ID名，直接剔除
        if (!judge.isMC_ID(args[0].contentToString())) {
            throw new Exception("非法的ID名");
        }

        if (new MysqlUtil().queryPlayer(senderID, "cells_whitelist").get("QQ_id") != null) {
            throw new Exception("你已经绑定id了，之前的id为"
                    + new MysqlUtil().queryPlayer(senderID, "cells_whitelist").get("MC_id")
                    + "，如需更改请联系管理员");
        }


        String fixedUUID, MC_id;
        //检查是否为正版online模式，目前默认离线模式，false
        if (config.getBoolean("bind-id-and-qq.online-mode")) {
            //todo 日后再补充正版模式
            //此处临时也写为离线模式方式
            MC_id = args[0].contentToString();
            fixedUUID = "";
        } else {
            //离线模式来处理
            MC_id = args[0].contentToString();
            fixedUUID = "offline";
        }

        //接下来将玩家的相关信息插入到白名单表中
        Object QQ_name = new MysqlUtil().queryPlayer(senderID, "cells_users").get("QQ_name");

        //将玩家名，qq，UUID等信息插入数据库的cells_whitelist表
        String order = String.format("insert into cells_whitelist (QQ_id, QQ_name, MC_id, uuid, bind_time) values (%s, '%s', '%s', '%s', '%s');"
                , senderID, QQ_name, MC_id, fixedUUID, judge.nowTime());
        new MysqlUtil().executeCommand(order);


        //检查是否开启了绑定ID后，修改群名片功能
        if (config.getBoolean("bind-id-and-qq.modify-namecard")) {
            //确认机器人bot是否有足够的群权限，来修改群成员的名片
            if (Objects.requireNonNull(bot.getGroup(config.getLong("player-group"))).getBotPermission().compareTo(MemberPermission.ADMINISTRATOR) >= 0) {
                Objects.requireNonNull(Objects.requireNonNull(bot.getGroup(config.getLong("player-group"))).get(senderID)).setNameCard(MC_id);
            } else {
                BotOperator.sendGroupMessage(config.getLong("player-group"), "请群管理员注意：bot修改群名片需要群管理员权限，请设置bot为群管理员");
            }
        }

        //如果白名单功能开启，则告诉发送指令的群成员，QQ已绑定ID且添加了白名单
        //否则，只单纯QQ绑定ID
        if (config.getBoolean("bind-id-and-qq.whitelist")) {
            return MessageUtils.newChain(new At(senderID)).plus(new PlainText(" 绑定ID成功，白名单已添加"));
        } else {
            return MessageUtils.newChain(new At(senderID)).plus(new PlainText(" 绑定ID成功"));
        }
    }

    //更新绑定的ID
    public MessageChain update(Long groupID, Long senderID, Message[] args, @Nullable QuoteReply quoteReply) throws PlayerNotFoundException, SQLException {
        Configuration config = ConfigManager.getConfig();

        //如果为离线模式，更新ID只能由服务器管理员操作
        if (!config.getBoolean("bind-id-and-qq.online-mode")) {
            return MessageUtils.newChain(new At(senderID), new PlainText(" 目前使用离线模式，无法与 mojang 服务器通信更新id，请联系管理员手动更新"));
        }

        //todo 如果为正版模式
        return null;
    }


    //解绑玩家的ID
    public MessageChain unbind(Long groupID, Long senderID, Message[] args, @Nullable QuoteReply quoteReply) throws Exception {
        Configuration config = ConfigManager.getConfig();

        //指令长度符合要求
        if (args.length < 1) {
            throw new InvalidSyntaxException();
        }
        //只有管理群的成员才能解绑玩家ID
        if (!(bot.getGroup(config.getLong("op-group")).contains(senderID))) {
            throw new NoPermissionException();
        }

//        //获取Long格式的QQ或者String格式的MC_Id
//        Object Id = judge.judgeMcQQ(args[0]);

        //先查询白名单上有无此成员
        Map<String, Object> map = new MysqlUtil().queryPlayer(args[0], "cells_whitelist");
        if (map.get("QQ_id") == null) {
            throw new PlayerNotFoundException();
        }
        String QQ_id = map.get("QQ_id").toString();
        String MC_id = map.get("MC_id").toString();

        try {
            // OP群的成员才能使用解绑指令
            if (!bot.getGroup(config.getLong("op-group")).contains(senderID)) {
                throw new Exception("非管理群成员，不能执行解绑指令");
            }

            // 不能解绑OP群内成员，除非发送指令的人是超级管理员
            if (bot.getGroup(config.getLong("op-group")).contains(Long.parseLong(QQ_id))) {
                if (!config.getLongList("super-admin-account").contains(senderID)){
                    throw  new Exception("你没有权限解绑一位管理群成员");
                }
            }
        } catch (NumberFormatException e) {
//            Main.instance.warn("QQ_id转化错误，如果出现此错误，代码还需要更改", e);
        }


        //删除数据库white_list表中对应的信息
        new MysqlUtil().executeCommand(String.format("delete from cells_whitelist where QQ_id = '%s'", QQ_id));

        try {
            Main.getInstance().getProxy().getPlayer(MC_id).disconnect(new TextComponent(String.format("你的ID绑定已解除")));
        } catch (Exception e) {
            log.info("被解绑的玩家目前不在服务器内，不执行踢出玩家");
        }

        return new MessageChainBuilder().append(new At(senderID)).append(" 已解除").append(MC_id).append(" 的ID绑定,白名单已移除").build();
    }


    //拉黑玩家
    public MessageChain ban(Long groupID, Long senderID, Message[] args, QuoteReply quoteReply) throws Exception {
        Configuration config = ConfigManager.getConfig();

        //指令长度符合要求
        //考虑到ban人的时候可能还会需要写清楚原因，和处罚时间，比如#ban testplayer 因为炸服
        //因此当args长度应当为不能大于2
        if (args.length > 2) {
            throw new InvalidSyntaxException();
        }
        //只有管理群的成员才能拉黑玩家ID
        if (!(bot.getGroup(config.getLong("op-group")).contains(senderID))) {
            throw new NoPermissionException();
        }

        //获取Long格式的QQ或者String格式的MC_Id
//        Object Id = judge.judgeMcQQ(args[0]);

        //先查询黑名单上有无此成员
        Map<String, Object> map = new MysqlUtil().queryPlayer(args[0], "cells_blacklist");
        //如果黑名单上有，则抛出错误，并把该成员被ban详细信息说出来
        if (map.get("QQ_id") != null || map.get("MC_id") != null) {
            throw new Exception("\n" + args[0] + " 已于" + map.get("created") + " 被ban\n" + "具体原因："
                    + map.get("reason") + "\n当时操作人：" + map.get("sources"));
        }

        String QQ_id = args[0].toString();
        String MC_id = args[0].toString();

        //如果黑名单上无，则在cells_blacklist表上增加信息
        //查询白名单是否有信息，如果有，将QQ_id号和MC_id替换为白名单中的数据
        Map<String, Object> mapWhite = new MysqlUtil().queryPlayer(args[0], "cells_blacklist");
        if (mapWhite.get("QQ_id") != null || mapWhite.get("MC_id") != null) {
            QQ_id = mapWhite.get("QQ_id").toString();
            MC_id = mapWhite.get("MC_id").toString();
        }

        //如果发送解绑指令的人不是超级管理员，同时解绑的玩家QQ为管理群成员，则无法拉黑OP的ID
        try {
            if (!config.getLongList("super-admin-account").contains(senderID) ||
                    Objects.requireNonNull(bot.getGroup(config.getLong("op-group"))).contains(Long.parseLong((QQ_id)))) {
                throw new InsufficientPermissionsException("你没有权限拉黑一位op的ID");
            }
        } catch (NumberFormatException e) {
//            Main.instance.warn("QQ_id转化错误，如果出现此错误，代码还需要更改", e);
        }

        String reason = "暂未记录";
        String expires = "暂未记录(留待开发)";
        if (args.length > 1) {
            reason = args[1].toString();
        }

        //将QQ_id，MC_id等信息插入数据库的cells_blacklist表
        String order = String.format("insert into cells_blacklist (QQ_id, Mc_id, created, sources, expires, reason) values ('%s', '%s', '%s', '%s', '%s','%s');"
                , QQ_id, MC_id, judge.nowTime(), senderID, expires, reason);
        new MysqlUtil().executeCommand(order);


        return MessageUtils.newChain(new At(senderID))
                .plus(new PlainText(" 已将 "))
                .plus(new PlainText(args[0].contentToString()))
                .plus(" 加入黑名单");
    }

    public MessageChain unban(Long groupID, Long senderID, Message[] args, @Nullable QuoteReply quoteReply) throws Exception {
        Configuration config = ConfigManager.getConfig();
        //指令长度符合要求
        if (args.length < 1) {
            throw new InvalidSyntaxException();
        }
        //只有管理群的成员才能unban玩家ID
        if (!(bot.getGroup(config.getLong("op-group")).contains(senderID))) {
            throw new NoPermissionException();
        }

        //先查询白名单上有无此成员
        Map<String, Object> map = new MysqlUtil().queryPlayer(args[0], "cells_blacklist");
        if (map.get("QQ_id") == null) {
            throw new PlayerNotFoundException();
        }

        //删除数据库cells_blacklist表中对应的信息
        new MysqlUtil().executeCommand(String.format("delete from cells_blacklist where QQ_id='%s' or MC_id='%s'", args[0], args[0]));

        return new MessageChainBuilder().append(new At(senderID)).append(" 已解除 ").append(args[0]).append(" 的牢狱").build();
    }

    @Override
    public MessageChain onEnable(@NotNull Long groupID, @NotNull Long senderID, Message[] args, @Nullable QuoteReply quoteReply) {
        return null;
    }

    @Override
    public Map<String, Object> register() throws NoSuchMethodException {
        Map<String, Object> info = new HashMap<>();
        Map<String, Method> commands = new HashMap<>();
        Map<String, String> usages = new HashMap<>();

        commands.put("id", BindQQandID.class.getMethod("bind", Long.class, Long.class, Message[].class, QuoteReply.class));
        commands.put("绑定", BindQQandID.class.getMethod("bind", Long.class, Long.class, Message[].class, QuoteReply.class));
        commands.put("deleteid", BindQQandID.class.getMethod("unbind", Long.class, Long.class, Message[].class, QuoteReply.class));
        commands.put("解绑", BindQQandID.class.getMethod("unbind", Long.class, Long.class, Message[].class, QuoteReply.class));
        commands.put("ban", BindQQandID.class.getMethod("ban", Long.class, Long.class, Message[].class, QuoteReply.class));
        commands.put("unban", BindQQandID.class.getMethod("unban", Long.class, Long.class, Message[].class, QuoteReply.class));

        usages.put("id", "#id <玩家ID> - 绑定ID");
        usages.put("绑定", "#绑定 <玩家ID> - 绑定ID");
        usages.put("deleteid", "#deleteid <玩家ID> - 解除一位玩家的ID绑定，并将此ID移出白名单");
        usages.put("解绑", "#解绑 <玩家ID> - 解除一位玩家的ID绑定，并将此ID移出白名单");
        usages.put("ban", "#ban <玩家ID或QQ> <可选被ban原因> - 将玩家及对应QQ拉入黑名单");
        usages.put("unban", "#unban <玩家ID或者QQ> - 解除黑名单");

        info.put("name", "BindIDAndQQ");
        info.put("commands", commands);
        info.put("usages", usages);
        info.put("author", "Zemise_");
        info.put("description", "进行绑定id的相关操作");
        info.put("version", "1.3");

        return info;
    }
}
