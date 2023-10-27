package com.zemise.cellsbot.waterfall.utils.DatabaseUtil;

import com.zemise.cellsbot.common.util.downloadUtil.ContinuousDownload;
import com.zemise.cellsbot.waterfall.Main;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class DownloadDatabaseIP {
    @Getter
    private static File databaseIP;

    public static void downloadDatabaseIP() {
        String databaseUrl = "https://gitcode.net/mirrors/lionsoul2014/ip2region/-/raw/master/data/ip2region.xdb";
        String savePath = Main.getInstance().getDataFolder().getPath();

        //避免因为下载IP数据而过长加载，使用异步的方式
        Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                ContinuousDownload.downloadFile(databaseUrl, savePath);
                log.info("IP数据库ip2region.xdb下载或者加载成功");
            }catch (IOException e){
                e.printStackTrace();
            }
            databaseIP = ContinuousDownload.getDownloadedFile();
        });
    }
}
