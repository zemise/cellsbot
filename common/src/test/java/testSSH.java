import com.zemise.cellsbot.common.util.downloadUtil.SshDownload;
import org.jetbrains.annotations.TestOnly;

/**
 * @Author Zemise_
 * @Date 2023/4/8
 * @Description 测试ssh下载远程主机文件
 */

public class testSSH {
    @TestOnly
    public static void main(String[] args) {
        String remoteHost = "remotehost";
        int port = 22;
        String username = "username";
        String password = "password";
        String remoteFilePath = "remotePath";
        String localFilePath = "localhost";

        try {
            SshDownload.downloadFile(remoteHost, port, username, password, remoteFilePath, localFilePath);
            System.out.println("文件已下载到本地路径：" + localFilePath);
        } catch (SshDownload.DownloadException e) {
            System.out.println(e);
        }
    }
}
