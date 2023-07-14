package com.zemise.cellsbot.common.util.miraiUtil;

import kotlin.coroutines.Continuation;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.QRCodeLoginListener;
import net.mamoe.mirai.network.LoginFailedException;
import net.mamoe.mirai.utils.LoginSolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

// 自定义LoginSolver
public class QqLoginSolver extends LoginSolver {
    public static void main(String[] args) {
        QRCodeLoginListener qrCodeLoginListener = new QqLoginSolver().createQRCodeLoginListener(BotFactory.INSTANCE.newBot(123456L, "123"));
    }
    private Object verification(Long qq, Object request) {
        //QqSession qqSession = QqSessionManager.getQqSession(qq);
        Object[] code = new Object[1];
        CountDownLatch latch = new CountDownLatch(1); // 定义一个CountDownLatch对象，用于等待code[0]
     /*   qqSession.sendRequest(request, response -> {
            code[0] = response;
            latch.countDown();
        });
        try {
            latch.await(300000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // 错误处理
        }*/
        return code[0];
    }
 
    @Nullable
    @Override
    public Object onSolvePicCaptcha(@NotNull Bot bot, @NotNull byte[] bytes, @NotNull Continuation<? super String> continuation) {
        return verification(bot.getId(), bytes);
    }
 
    @Nullable
    @Override
    public Object onSolveSliderCaptcha(@NotNull Bot bot, @NotNull String url, @NotNull Continuation<? super String> continuation) {
        return verification(bot.getId(), url);
    }
 
    @NotNull
    @Override
    public QRCodeLoginListener createQRCodeLoginListener(@NotNull Bot bot) {
        return new QRCodeLoginListener() {
 
            @Override
            public void onStateChanged(@NotNull Bot bot1, @NotNull State state) throws LoginFailedException {
                if (state.name().equals("TIMEOUT")) throw new RuntimeException("二维码已失效");
            }
 
            @Override
            public void onFetchQRCode(@NotNull Bot bot1, @NotNull byte[] bytes) {
                //QqSessionManager.getQqSession(bot1.getId()).sendRequest(bytes, null);
            }
        };
    }
}