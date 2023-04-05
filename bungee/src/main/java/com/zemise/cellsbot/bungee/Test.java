package com.zemise.cellsbot.bungee;



import com.zemise.cellsbot.common.util.downloadUtil.ContinuousDownload;

import java.io.IOException;

/**
 * @Author Zemise_
 * @Date 2023/3/31
 * @Description 测试类001
 */

public class Test {

    public static void main(String[] args) throws IOException {

        String databaseUrl = "https://gitcode.net/mirrors/lionsoul2014/ip2region/-/raw/master/data/ip2region.xdb";
        String savePath = "/Volumes/passSSD/CodeProject/ZemBotProject/CellsBot/ATest";
        ContinuousDownload.downloadFile(databaseUrl,savePath);
        try {
            ContinuousDownload.downloadFile(databaseUrl, savePath);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
