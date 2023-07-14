package com.zemise.cellsbot.common.plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftServerParser {

    private static final Pattern IP_PATTERN = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");
    private static final Pattern HOSTNAME_PATTERN = Pattern.compile("(?<=^)[^:]+(?=:\\d{1,5}$)");
    private static final Pattern PORT_PATTERN = Pattern.compile("\\d{1,5}$");


    public static String getServerIP(String input) {
        Matcher matcher = IP_PATTERN.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String getServerHostname(String input) {
        Matcher matcher = HOSTNAME_PATTERN.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static Integer getServerPort(String input) {
        Matcher matcher = PORT_PATTERN.matcher(input);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return null;
    }

}
