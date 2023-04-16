import com.zemise.cellsbot.common.configuration.file.FileConfiguration;
import com.zemise.cellsbot.common.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.TestOnly;

import java.io.File;

/**
 * @Author Zemise_
 * @Date 2023/4/8
 * @Description 测试yml文件加载
 */
public class testDatabaseConfig {
    @TestOnly
    public static void main(String[] args) {
        FileConfiguration database = YamlConfiguration.loadConfiguration(
                new File("localpath/database.yml"));

        System.out.println(database.getKeys(true));

    }
}
