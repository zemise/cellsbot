package com.zemise.cellsbot.common.evnet;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean var1);
}