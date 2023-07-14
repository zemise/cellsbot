package com.zemise.cellsbot.common.plugin;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.plugin.mcping.MCPing;
import com.zemise.cellsbot.common.plugin.mcping.MCPingOptions;
import com.zemise.cellsbot.common.plugin.mcping.MCPingResponse;
import com.zemise.cellsbot.common.plugin.screenshot.apishot.apiScreenShot;
import com.zemise.cellsbot.common.plugin.screenshot.seleshot.ScreenShotMachine;
import com.zemise.cellsbot.common.plugin.screenshot.seleshot.ScreenshotOptions;

import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.ImageService;
import com.zemise.cellsbot.common.util.miraiUtil.MiraiGroup;
import com.zemise.cellsbot.common.util.miraiUtil.MiraiMessageUtil;

import io.github.zemise.entity.AudioMachine;
import io.github.zemise.entity.AudioOption;
import io.github.zemise.entity.Speaker;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import org.openqa.selenium.Proxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Zemise_
 * @Date 2023/4/22
 * @Description Q群消息事件处理
 */
public class QGroupMessageEvent extends SimpleListenerHost {

    @EventHandler
    public void onGroupMessageEvent(GroupMessageEvent e) {
        Configuration config = ConfigManager.getConfig();

        List<Long> longList = config.getLongList("player-group");
        // todo 只监听指定群
        Long groupId = MiraiMessageUtil.getGroupId(e);
        Long senderID = MiraiMessageUtil.getSenderId(e);
        // todo 此处有问题
        //String textString = MiraiMessageUtil.getContent(e);
        // todo 暂时先这么改
        String textString = e.getMessage().contentToString().trim();


        //存在@机器人的消息就向ChatGPT提问
        if (MiraiMessageUtil.isAtBot(e) || textString.contains(e.getBot().getNick())) {
            String reply = "";
            //去除@再提问
            String askMessage = textString.replace("@" + e.getBot().getId(), "").trim() + ";";

            reply = recordGPT.onGPT(e, groupId, senderID, askMessage);


            Speaker[] speakers = Speaker.values(); // 获取枚举类中的所有值
            Random random = new Random();
            Speaker randomSpeaker = speakers[random.nextInt(speakers.length)]; // 随机获取其中一个值

            String speakerName = "小燕";
            switch (randomSpeaker) {
                case XIAOYAN -> {
                    speakerName = "小燕";
                }
                case AISJIUXU -> {
                    speakerName = "小许";
                }
                case AISXPING -> {
                    speakerName = "小萍";
                }
                case AISJINGER -> {
                    speakerName = "小婧";
                }
                case AISBABYXU -> {
                    speakerName = "小宝";
                }
                default -> randomSpeaker = Speaker.XIAOYAN;
            }

            AudioOption option = AudioOption.builder()
                    .appid("001a5b2b")
                    .apiSecret("NjU1NDA1ZjEwZTE1MTFiMjA1YzRiOTBk")
                    .appKey("47d97152237d46508879d10dd52beaf5")
                    .speaker(randomSpeaker)
                    .build();

            try {
                new MiraiGroup(BotOperator.getBot(), e.getGroup().getId()).sendMessage("我是：" + speakerName + "，请稍等...");
                new AudioMachine(option).generateAudio(reply, "out.mp3");

                new MiraiGroup(BotOperator.getBot(), e.getGroup().getId()).sendMessage(reply);
                new MiraiGroup(BotOperator.getBot(), e.getGroup().getId()).sendAudio(new File("out.mp3"));
                return;
            } catch (Exception es) {
                es.printStackTrace();
            }
        }

        // 如果存在指令符开头
        //if(MiraiMessageUtil.startWithKeywords(e, config.getString("bot-command-prefix"))){
        if (MiraiMessageUtil.startWithKeywords(e, "#please")) {
            MessageChain helpMsg = MiraiMessageUtil.getNoCommandMessage(e, "#please");

            helpAsk.please(e, groupId, senderID, helpMsg);
        }



        if (MiraiMessageUtil.startWithKeywords(e, "mc ")) {
            String host = textString.replace("mc ", "");
            System.out.println(host);
            int port = 25565;
            Integer serverPort = MinecraftServerParser.getServerPort(textString);
            if (serverPort != null) {
                port = serverPort;
            }

            MCPingOptions options;
            try {
                options = MCPingOptions.builder()
                        .hostname(host)
                        .port(port)
                        .build();
            } catch (Exception ex) {
                options = MCPingOptions.builder()
                        .hostname(MinecraftServerParser.getServerHostname(host))
                        .port(port)
                        .build();
            }


            MCPingResponse reply;

            ArrayList<String> list = new ArrayList<>();

            try {
                reply = MCPing.getPing(options);
            } catch (IOException ex) {
                BotOperator.sendGroupMessage(groupId, options.getHostname() + " is down or unreachable.");
                return;
            }


            MCPingResponse.Description description = reply.getDescription();
            list.add(description.getStrippedText() + "\n");

            MCPingResponse.Players players = reply.getPlayers();

            list.add("Players: \n");
            list.add("    Online count: " + players.getOnline());
            list.add("\n    Max players: " + players.getMax());


            MCPingResponse.Version version = reply.getVersion();
            list.add("\nVersion: ");
            list.add("\n    Protocol: " + version.getProtocol());
            list.add("\n    Name: " + version.getName());

            String message = description.getStrippedText() + "\n" + "Players: \n" + "    Online count: " + players.getOnline()
                    + "\n    Max players: " + players.getMax() + "\nVersion: " + "\n    Protocol: " + version.getProtocol()
                    + "\n    Name: " + version.getName();


            BotOperator.sendGroupMessage(groupId, message);

        }

        // 监听网页链接
        String webUrl = findWebsiteLink(textString);

        if (webUrl != null) {
            if (groupId == 780594692L & (!webUrl.contains("mirai") && !webUrl.contains("github"))) {
                return;
            }

            String imagePath = "out.png";

            try {
                // sele screenshot
                String proxyServer = "localhost:7890";
                Proxy proxy = new Proxy();
                proxy.setHttpProxy(proxyServer).setHttpProxy(proxyServer).setSslProxy(proxyServer);

                ScreenshotOptions options = ScreenshotOptions.builder()
                        .height(1366)
                        .width(2000)
                        .waitTime(2500)
                        .proxy(proxy)
                        .build();

                ScreenShotMachine screenShot = new ScreenShotMachine("/root/chromedriver", options);
                screenShot.takePNG(webUrl, imagePath);
                screenShot.close();
            } catch (Exception exception) {
                try {
                    // api screenshot
                    apiScreenShot screenShot = new apiScreenShot("b2cc16&url=screenshotmachine.com&dimension=1024x768");
                    screenShot.takePNG(webUrl, imagePath);
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                }
            }

            ImageService imageService = new ImageService();
            Image image = imageService.uploadImage(imagePath, BotOperator.getBot());
            MessageChain singleMessages = imageService.parseMsgChainByImg(image);
            BotOperator.sendGroupMessage(groupId, singleMessages);
        }
    }

    public static String findWebsiteLink(String input) {
        String regex = "(?i)\\b((?:https?://|www\\d*\\.)\\S+\\b)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String link = matcher.group();
            if (link.startsWith("www")) {
                link = "http://" + link;
            }
            return link;
        }
        return null;
    }
}
