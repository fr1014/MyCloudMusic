package com.fr1014.mycoludmusic.http.interceptor;

import com.fr1014.mycoludmusic.utils.AccountUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by fanrui on 2021/1/14
 * Describe:
 */
public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> cookies = (HashSet<String>) AccountUtils.INSTANCE.getCookie();
        if (cookies != null && cookies.size() > 0) {
            for (String cookie : cookies) {
                builder.addHeader("Cookie", cookie);
            }
        }
        return chain.proceed(builder.build());
    }
}

