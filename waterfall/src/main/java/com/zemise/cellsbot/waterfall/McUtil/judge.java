package com.zemise.cellsbot.waterfall.McUtil;


import net.mamoe.mirai.message.data.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class judge {
    /**
     * 用来判断mirai的Message args具体是QQ号还是MC_id
     *
     * @param args Mirai的Message格式变量
     * @return Id 如果是符合QQ号格式的，则返回Long类型的Id，否则还是字符串
     */
    public static Object judgeMcQQ(Message args){
        //传入的参数args可能是QQ号，也可能是Mc的ID
        Object Id = args.contentToString();
        //正则表达式  QQ号5至15位
        // 第一位1-9  后4-14位0-9
        String qqPattern = "^[1-9][0-9]{4,10}$";

        //如果传入的args为QQ账号格式，则将其强转为long
        if(Pattern.matches(qqPattern, args.contentToString())){
            Id = Long.valueOf(args.contentToString());
        }

        return Id;
    }

    /**
     * 判断字符串是否符合Minecraft的Id格式
     * @param Id 需要查询的字符串
     * @return boolean
     */
    public static boolean isMC_ID(String Id){
        String pattern = "^[a-zA-Z0-9_]{3,16}$";
        return Pattern.matches(pattern, Id);
    }

    /**
     * 获得当前时间的字符串
     * @return 当前时间字符串
     */
    public static String nowTime() {


        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);

//        //时间信息
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return now.format(dateTimeFormatter);
    }
}
