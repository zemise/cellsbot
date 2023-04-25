package com.zemise.cellsbot.common.plugin;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.plugin.chatgpt.ChatGPT;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GPT {
    public String askGPT(String message) {
        Configuration config = ConfigManager.getConfig();
        String response;
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS).proxy(proxy).build();

        try {
            ChatGPT chatGPT = new ChatGPT(config.getString("apiKey"), client);
            response = chatGPT.ask(message).replaceFirst("^[\n\r]+", "");
        } catch (Exception e) {
            response = e.getMessage();
        }

        return response;
    }


    /**
     * 判断询问消息是否符合预设的问答消息
     *
     * @param askMessage 问题消息
     * @return 预设的回消息，如果没有，则返回问题消息
     */
    public static String containKey(String askMessage) {
        Configuration replyConfig = ConfigManager.getAutoReplyConfig();
        List<String> replyList = replyConfig.getStringList("GPT.question-reply");

        Map<String, String> map = replyList.stream()
                .map(s -> s.split("-"))
                .collect(Collectors.toMap(
                        a -> a[0],
                        a -> a[1]
                ));
        Set<String> strings = map.keySet();

        String messages = askMessage.toLowerCase();

        for (String s : strings) {
            if (messages.contains(s)) {
                askMessage = map.get(s);
                break;
            }
        }
        return askMessage;
    }

}
