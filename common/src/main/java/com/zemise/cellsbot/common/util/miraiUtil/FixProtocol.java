package com.zemise.cellsbot.common.util.miraiUtil;

import net.mamoe.mirai.utils.BotConfiguration;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author <a href= "https://github.com/zemise">Zemise</a>
 * @since 2023/7/17
 */
public class FixProtocol {
    public static void main(String[] args) {

    }
    // 获取协议版本信息 你可以用这个来检查update是否正常工作
    public static Map<BotConfiguration.MiraiProtocol, String> info() {
        return FixProtocolVersion.info();
    }
}
