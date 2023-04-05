package com.zemise.cellsbot.common.evnet.entity;

import com.zemise.cellsbot.common.evnet.Event;

public class ServerDisconnectEvent extends Event {

    public ServerDisconnectEvent() {

    }

    public String toString() {
        return "ServerDisconnectEvent(player=" + ", target=" + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerDisconnectEvent;
    }

    public int hashCode() {

        int result = 1;

        return result;
    }
}