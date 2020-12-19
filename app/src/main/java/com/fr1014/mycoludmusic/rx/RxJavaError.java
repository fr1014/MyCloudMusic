package com.fr1014.mycoludmusic.rx;

import android.util.Log;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Create by fanrui on 2020/12/10
 * Describe:RxJava 全局异常捕获
 */
public class RxJavaError {

    /**
     * RxJava2 当取消订阅后(dispose())，
     * RxJava 抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),
     * 全部由 RxJavaPlugin 接收，需要提前设置 ErrorHandler.
     */
    public static void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // throwable.printStackTrace();
                if (null != throwable)
                    Log.e("MyApplication", "setRxJavaErrorHandler " + throwable.getMessage());
            }
        });
    }
}
