package com.fr1014.mycoludmusic.http;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private static final int LENGTH = 4000;

    @Override
    public void log(String message) {
//        Log.d("---HttpLogInfo", message);
        if (message.length() > LENGTH) {
            for (int i = 0; i < message.length(); i += LENGTH) {
                if (i + LENGTH < message.length()) {
                    Log.d("---helloHttp",message.substring(i, i + LENGTH));
                } else {
                    Log.d("---helloHttp",message.substring(i, message.length()));
                }
            }
        } else {
            Log.d("---helloHttp", message);
        }
    }

    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }
}
