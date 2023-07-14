package com.zemise.cellsbot.member;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Zemise_
 * @Date 2023/5/14
 * @Description
 */
public class McPlayer extends Person {
    @Getter
    @Setter
    private String mcID, nickname, UUID, ip, joinTime;

    @Getter
    @Setter
    private Long qqID;

    @Getter
    @Setter
    private int permission = 0;


    public McPlayer(String name, String mcID, Long qqID) {
        super(name);
        this.mcID = mcID;
        this.qqID = qqID;
    }

    public McPlayer(String name, String mcID, String nickname, String UUID, String ip, String joinTime, Long qqID, int permission) {
        super(name);
        this.mcID = mcID;
        this.nickname = nickname;
        this.UUID = UUID;
        this.ip = ip;
        this.joinTime = joinTime;
        this.qqID = qqID;
        this.permission = permission;
    }
}
