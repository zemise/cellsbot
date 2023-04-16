package com.zemise.cellsbot.listener;

import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zemise_
 * @Date 2023/3/17
 * @Description 监听撤回消息
 */


// todo 目前这个类，如果设备为watch，那么无法保存撤回消息，具体原因未知
@Slf4j
public class onRecall extends SimpleListenerHost {
    /**
     * 群消息存储map
     * key:groupId:messageIds
     */
    private Map<String, String> map = new ConcurrentHashMap<>();

    // 群消息消息最大存储数
    private static final int MAX_SIZE = 10000;

    /**
     * 获取消息的id
     *
     * @param messageChain 消息
     * @return id
     */
    private String getMessageIds(MessageChain messageChain) {
        // 处理事件
        int start = 15;
        String messageIds = "";
        String message = messageChain.toString();
        for (int i = start; i < message.length(); i++) {
            if (message.charAt(i) == ']') {
                messageIds = message.substring(start, i);
                break;
            }
        }
        return messageIds;
    }


    @EventHandler
    public void recordMessage(GroupMessageEvent event) {

        Group group = event.getGroup();
        Member sender = event.getSender();
        System.out.println(group);
        System.out.println(sender);

        System.out.println("测试008");

        MessageChain messageChain = event.getMessage();
        String messageIds = getMessageIds(event.getMessage());

        if (map.size() > MAX_SIZE) {
            map = new HashMap<>(1000);
        }

        map.put(event.getGroup().getId() + ":" + messageIds, messageChain.serializeToMiraiCode());

    }

    /**
     * 撤回专用，针对语音做了处理
     *
     * @param event      撤回事件
     * @param messageIds 撤回的消息id
     * @return 消息
     */

    public MessageChain getMessage(MessageRecallEvent.GroupRecall event, String messageIds) {
        String groupId = event.getGroup().getId() + "";
        String message = map.get(groupId + ":" + messageIds);

        if (message != null) {
            return MiraiCode.deserializeMiraiCode(message);
        }
        return null;
    }

    private String intsToString(int[] ints) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("ds=[");
        for (int i : ints) {
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }


    @EventHandler
    public void onRecallMessage(MessageRecallEvent.GroupRecall event) {
        log.info("test");
        System.out.println("测试000");
        long groupId = event.getGroup().getId();

        //boolean recallEnable = config.getBoolean("onRecallMessage.enable");
        //List<Long> onRecallGroupList = config.getLongList("onRecallMessage.groups");

        //if(!recallEnable | !onRecallGroupList.contains(groupId)) return;

        long operatorId = event.getOperator().getId();
        Bot bot = BotOperator.getBot();
        log.info("测试001");
        MemberPermission permission = bot.getGroup(event.getGroup().getId()).get(operatorId).getPermission();
        int level = permission.getLevel();
        log.info("测试002");

        if (level > 0) {
            String message = "[报告]：有人撤回消息\n\n[账号]：%s\n[内容]：%s";
            Long authorId = event.getAuthorId();
            String content = "无内容！害！想什么呢，这波撤回是管理员操作的";
            log.info(message, authorId, content);
            //BotOperator.sendGroupMessage(groupId, String.format(message, authorId, content));
            return;
        }

        try {
            // 获得撤回消息的ID
            String messageIds = intsToString(event.getMessageIds());
            // 获得撤回消息内容
            MessageChain message = getMessage(event, messageIds);

            MessageChainBuilder msg = new MessageChainBuilder();
            msg.append("[报告]：有人撤回消息\n")
                    .append("\n[账号]：" + event.getAuthorId() + "(" + event.getAuthor().getNick() + ")\n")
                    .append("[内容]：")
                    .append(message);

            log.info("测试", msg.build());
            //BotOperator.sendGroupMessage(event.getGroup().getId(), msg.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
