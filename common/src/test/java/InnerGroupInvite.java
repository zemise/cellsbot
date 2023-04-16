import com.zemise.cellsbot.common.configuration.file.FileConfiguration;
import com.zemise.cellsbot.common.configuration.file.YamlConfiguration;
import com.zemise.cellsbot.common.util.NumberConversions;
import com.zemise.cellsbot.common.util.databaseUtil.MysqlUtil;
import com.zemise.cellsbot.common.util.downloadUtil.SshDownload;

import java.io.File;
import java.util.Set;

/**
 * @Author Zemise_
 * @Date 2023/4/8
 * @Description 测试邀请玩家进入内群
 */
public class InnerGroupInvite {
    public static void main(String[] args) {
        new InnerGroupInvite().inviteMember();
    }


    // 因为数据库不同，所以先来一个大致的流程，后续视情况优化
    // 1. 获取Pure01的在线时间插件的yml文件，读取其中的所有uuid和在线时间
    // 2. 在线时间大于一定值的，返回所有uuid
    // 3. 查询LuckPerms玩家数据库，根据上一步uuid获得MC_id
    // 4. 再根据MC_id查询白名单获取QQ号，邀请入内群
    public void inviteMember() {

        // 下载文件
        String remoteHost = "localhost";
        int port = 22;
        String username = "name";
        String password = "password";
        String remoteFilePath = "remotepath/database.yml";
        String localFilePath = "localpath/database.yml";

        File dataFile = new File(localFilePath);

        try {
            SshDownload.downloadFile(remoteHost, port, username, password, remoteFilePath, localFilePath);
            System.out.println("文件已下载到本地路径：" + localFilePath);
        } catch (SshDownload.DownloadException e) {
            System.out.println(e);
        }

        FileConfiguration database = YamlConfiguration.loadConfiguration(dataFile);
        Set<String> keys = database.getKeys(false);

        MysqlUtil mysqlUtil = new MysqlUtil("remoteIP", 3306,
                "database", "root", "password");

        System.out.println("============");
        for (String key : keys) {
            String totalPlayTime = database.getString(key + ".total_play_time");
            int totalPlay = NumberConversions.toInt(totalPlayTime);

            if(totalPlay > 172800000){
                //Map<String, Object> stringObjectMap = mysqlUtil.queryColumn(key, "luckperms_players", "uuid");
                //if(stringObjectMap!=null){
                //    System.out.println(stringObjectMap);
                //}
            }
        }

    }
}
