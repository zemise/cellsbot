//package com.zemise.cellsbot.common.util.miraiUtil;
//
//import net.mamoe.mirai.contact.Group;
//import net.mamoe.mirai.message.data.Voice;
//import net.mamoe.mirai.utils.ExternalResource;
//
//import java.io.File;
//import java.io.IOException;
//
///**
// * @Author Zemise_
// * @Date 2023/5/24
// * @Description mirai上传语音与发送
// */
//public class VoiceService {
//    public Voice uploadVoice(String localPath, Group group) {
//        File file = new File(localPath);
//        if (!file.exists()) {
//            return null;
//        }
//        ExternalResource externalResource = ExternalResource.create(file);
//        ExternalResource.create()
//
//        Voice voice = group.uploadAudio(externalResource);
//        try {
//            externalResource.inputStream().close();
//            externalResource.close();
//            //System.out.println("关闭语音流成功");
//            return voice;
//        } catch (IOException e) {
//            System.out.println("关闭语音流出现异常");
//            e.printStackTrace();
//            return voice;
//        }
//    }
//}
