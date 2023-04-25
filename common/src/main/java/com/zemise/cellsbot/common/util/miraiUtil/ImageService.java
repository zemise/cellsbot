package com.zemise.cellsbot.common.util.miraiUtil;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;

/**
 * @Author zemise_
 * @Date 2023/3/17
 * @Description mirai上传图片与发送
 */
public class ImageService {
    /**
     * 解释图片为message
     * @param image 图片
     * @return 图片信息
     */
    public MessageChain parseMsgChainByImg(Image image) {
        MessageChain messageChain = MessageUtils.newChain();
        if (image == null) {
            messageChain = messageChain.plus("发送图片失败");
        } else {
            messageChain = messageChain.plus(image);
        }
        return messageChain;
    }

    /**
     * 上传图片
     *
     * @param localPath 本地图片地址
     * @param event mirai消息事件
     * @return 图片
     */
    public Image uploadImage(String localPath, MessageEvent event) {
        ContactList<Group> groupList = event.getBot().getGroups();
        Group group = null;
        for (Group groupTemp : groupList) {
            group = groupTemp;
            break;
        }
        if (group == null) {
            return null;
        }
        ExternalResource externalResource = ExternalResource.create(new File(localPath));
        Image image =  group.uploadImage(externalResource);
        try {
            externalResource.inputStream().close();
            externalResource.close();
            return image;
        } catch (IOException e) {
            System.out.println("关闭图片流出现异常");
            e.printStackTrace();
            return image;
        }
    }

    /**
     * 上传图片
     *
     * @param localPath 本地图片地址
     * @param bot mirai登陆的bot
     * @return 图片
     */
    public Image uploadImage(String localPath, Bot bot) {
        ContactList<Group> groupList = bot.getGroups();
        Group group = null;
        for (Group groupTemp : groupList) {
            group = groupTemp;
            break;
        }
        if (group == null) {
            return null;
        }
        ExternalResource externalResource = ExternalResource.create(new File(localPath));
        Image image =  group.uploadImage(externalResource);
        try {
            externalResource.inputStream().close();
            externalResource.close();
            return image;
        } catch (IOException e) {
            System.out.println("关闭图片流出现异常");
            e.printStackTrace();
            return image;
        }
    }
}
