package com.fr1014.mycoludmusic.http.interceptor;

import android.util.Log;

import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.utils.AccountUtils;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by fanrui on 2021/1/14
 * Describe:
 */
public class ReceivedCookiesInterceptor implements Interceptor {

    public ReceivedCookiesInterceptor() {
        super();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().url().getPath();
        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (path.contains("login") || path.contains("verify") && !originalResponse.headers("Set-Cookie").isEmpty()) {

            HashSet<String> cookies = new HashSet<>();
            for (String header : originalResponse.headers("Set-Cookie")) {
                Log.d("hello", "拦截的cookie是：" + header);
                cookies.add(header);
            }
            //保存的sharepreference文件名为cookie
            AccountUtils.INSTANCE.saveCookies(cookies);
        }

        return originalResponse;
    }
}
