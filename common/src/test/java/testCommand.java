import com.zemise.cellsbot.common.util.CommandRunner;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zemise_
 * @Date 2023/4/14
 * @Description 测试终端指令
 */

public class testCommand {
    @TestOnly
    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> strings = new ArrayList<>();
        strings.add("ping remoteHostIp");

        new CommandRunner(strings).runCommands();
    }
}
