package com.zemise.cellsbot.waterfall.plugins;

import com.zemise.cellsbot.common.util.miraiUtil.BotOperator;
import com.zemise.cellsbot.common.util.miraiUtil.ImageService;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.CommandNotFoundException;
import com.zemise.cellsbot.common.util.miraiUtil.exceptions.InvalidSyntaxException;
import com.zemise.cellsbot.waterfall.Main;
import net.mamoe.mirai.message.data.*;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class PluginManager {
    @SuppressWarnings("unchecked")
    public static MessageChain commandHandler(String command, Long groupID, Long senderID, Message[] args, QuoteReply quoteReply) throws Exception {
        Reflections reflections = new Reflections("com.zemise.cellsbot.waterfall.plugins");

        //通过Reflections的getSubTypes方法获取mainPlugin类下的所有子类，Set集合
        Set<Class<? extends mainPlugin>> subPlugins = reflections.getSubTypesOf(mainPlugin.class);

            //判断传入的command的字符串是何种指令，具体实现
            switch (command) {

                case "help", "菜单" -> {
                    //重写
                    ImageService imageService = new ImageService();
                    String path = Main.instance.getDataFolder().getPath()+"/cells.png";
                    Image imageHelp = imageService.uploadImage(path, BotOperator.getBot());

                    return imageService.parseMsgChainByImg(imageHelp);
                }
                default -> {
                    for (Class<? extends mainPlugin> subClass : subPlugins) {
                        //返回子类里的方法的Map集合
                        Map<String, Object> pluginInfo = (Map<String, Object>) subClass.getMethod("register").invoke(subClass.getDeclaredConstructor().newInstance());
                        //取得集合里的指令命令
                        Map<String, Method> pluginCommands = (Map<String, Method>) pluginInfo.get("commands");

                        if (pluginCommands.containsKey(command)) {
                            try {
                                return (MessageChain) pluginCommands.get(command).invoke(subClass.getDeclaredConstructor().newInstance(), groupID, senderID, args, quoteReply);
                            } catch (InvocationTargetException e) {
                                MessageChainBuilder errorMsg = new MessageChainBuilder();
                                errorMsg.append(new At(senderID)).append(" ");

                                if (e.getTargetException() instanceof InvalidSyntaxException) {
                                    Map<String, String> pluginUsage = (Map<String, String>) pluginInfo.get("usages");
                                    errorMsg.append(" 语法错误\n用法： ").append(pluginUsage.get(command));
                                } else if (e.getTargetException() instanceof SQLException) {
                                    errorMsg.append(" 数据库繁忙，请稍后重试");
                                    e.getTargetException().printStackTrace();
                                } else {
                                    errorMsg.append(e.getTargetException().getMessage());
                                    e.getTargetException().printStackTrace();
                                }
                                return errorMsg.build();
                            }
                        }
                    }
                }
            }


            throw new CommandNotFoundException();
    }
}
