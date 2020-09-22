package com.fr1014.mymvvm.base;

import android.app.Application;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance(){
        if (instance == null){
            throw new NullPointerException("please inherit BaseApplication");
        }
        return instance;
    }
}
