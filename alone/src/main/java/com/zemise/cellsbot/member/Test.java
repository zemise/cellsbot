package com.zemise.cellsbot.member;

import ch.qos.logback.core.sift.AppenderFactoryUsingSiftModel;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("此时一名QQ为123456，名字叫做 Wei 的新人申请进群，ID名为：Alex");

        // 对ID名进行判断，如果为正版ID，则直接同意进群

        McPlayer newer = new McPlayer("Wei", "Alex", 123456L);
        // 给其设定其他的参数
        newer.setJoinTime("20.22.11");
        newer.setPermission(2);


        // QQ机器人登陆后，将数据库中的玩家信息写入数组，方便之后的查询
        // todo 第一件事，就是监听QQ机器人的登陆事件
    }
}
