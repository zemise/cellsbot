package com.zemise.cellsbot.common.plugin;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.MiraiMessageUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Zemise_
 * @Date 2023/4/6
 * @Description
 */
public class recordGPT{
    /**
     * 群消息存储map
     * key:idQQ:ask+respon
     */
    private static Map<Long, String> map = new ConcurrentHashMap<>();

    /**
     * 群成员已发问次数存储
     * key:idQQ:ask-time
     */
    private static Map<Long, Integer> mapTime = new ConcurrentHashMap<>();

    // GPT消息消息最大存储数
    private static final int MAX_SIZE = 10000;

    /**
     * 获取字符串前50个字符
     *
     * @param str
     * @return 缩减的字符串
     */
    public static String truncateString(String str, int length) {
        if (str.length() <= length) {
            return str;
        } else {
            return str.substring(0, length);
        }
    }

    public static void recordQuestion(Long senderID, String response) {
        if (map.size() > MAX_SIZE) {
            map = new HashMap<>(1000);
        }
        map.put(senderID, response);
    }

    public static void onGPT(GroupMessageEvent e, Long groupId, Long senderID, String askMessage) {
        Collection<String> keyWords = new ArrayList<>();
        keyWords.add("reset");
        keyWords.add("忘记");

        if (MiraiMessageUtil.startWithKeywords(e, keyWords)) {
            map.remove(senderID);
            BotOperator.sendGroupMessage(groupId, MiraiMessageUtil.quoteReply(e, "已重置与您的对话"));
            return;
        }
        // 匹配预设问答
        String s = GPT.containKey(askMessage);
        // 如果包含预设问答的问题内容，则发送预设的回答
        if (!s.equals(askMessage)) {
            BotOperator.sendGroupMessage(groupId, MiraiMessageUtil.quoteReply(e, s));
            return;
        }

        String finalAsk = "";
        if (map.get(senderID) == null) {
            finalAsk = askMessage;
            mapTime.put(senderID, 1);
        } else {
            finalAsk = "基于以下对话，你来扮演-，你来回答最后的提问:" + map.get(senderID) + "\n提问：" + askMessage + ";";
            if (finalAsk.length() > ConfigManager.getConfig().getInt("maxLength")) {
                finalAsk = askMessage;
            }

            if (mapTime.get(senderID) > ConfigManager.getConfig().getInt("maxAsk")){
                BotOperator.sendGroupMessage(groupId,MiraiMessageUtil.quoteReply(e, " 今日您询问的次数已达上限"));
                return;
            }

            mapTime.put(senderID, mapTime.get(senderID) + 1);
        }

        String response = new GPT().askGPT(finalAsk);

        if (response.contains("timeout") || response.contains("Remote host terminated") || response.contains("Connection reset")) {
            response = "抱歉，或许是网络问题，和您的对话已经重置，请尝试重新发问";
            map.remove(senderID);

        } else {
            String aSay = "A说 " + askMessage;
            String responseRecord = truncateString(response, ConfigManager.getConfig().getInt("recordLength"));
            String bSay = "- " + responseRecord + ";";
            String conversation = aSay + bSay;
            recordQuestion(senderID, map.get(senderID) + conversation);
        }

        BotOperator.sendGroupMessage(groupId, MiraiMessageUtil.quoteReply(e, response));
    }
}