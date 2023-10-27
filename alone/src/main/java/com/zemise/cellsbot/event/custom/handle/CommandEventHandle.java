package com.zemise.cellsbot.event.custom.handle;

import com.zemise.cellsbot.common.ConfigManager;
import com.zemise.cellsbot.common.configuration.Configuration;
import com.zemise.cellsbot.common.evnet.EventHandler;
import com.zemise.cellsbot.event.custom.CommandMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.QuoteReply;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description 处理各类指令消息
 */
public class CommandEventHandle {
    // todo 第一点是需要知道指令消息具体是发送了什么指令，要去掉前缀符号
    // 指令消息的格式往往是 (prefix)指令开头 具体指令

    @EventHandler
    public void onCommandReceive(CommandMessageEvent event) {
        Configuration config = ConfigManager.getConfig();

        try {
            String textString = event.getCommandMessageEvent().getMessage().contentToString().trim();

            if (!textString.startsWith(config.getString("bot-command-prefix"))) {
                return;
            }

            //按空格分割，并去掉前缀字符，获得具体指令字符串
            String command = textString.split(" ", 2)[0].substring(1);

            List<Message> argsList = new ArrayList<>();
            for (Message message : event.getCommandMessageEvent().getMessage()) {
                if (message instanceof PlainText) {
                    for (String arg : message.contentToString().split(" ")) {
                        argsList.add(new PlainText(arg));
                    }
                } else {
                    argsList.add(message);
                }
            }

            argsList.remove(0);
            argsList.remove(0);

            QuoteReply quoteReply = event.getCommandMessageEvent().getMessage().get(QuoteReply.Key);

            Message[] args = argsList.toArray(new Message[0]);

            MessageChainBuilder message = new MessageChainBuilder();


            System.out.println("指令" + command);
            System.out.println("群" + event.getCommandMessageEvent().getGroup());
            System.out.println("发的人" + event.getCommandMessageEvent().getSender());
            System.out.println("分段指令" + args);
            System.out.println("引用" + quoteReply);

        } catch (Exception es) {
            es.printStackTrace();
        }
    }
}
