package com.fr1014.mycoludmusic.eventbus;

/**
 * Create by fanrui on 2021/1/12
 * Describe:
 */
public class LoginStatusEvent {
    public boolean isLogin;

    public LoginStatusEvent(boolean isLogin){
        this.isLogin = isLogin;
    }
}
