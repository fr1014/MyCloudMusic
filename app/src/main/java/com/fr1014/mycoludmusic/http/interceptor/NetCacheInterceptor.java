package com.fr1014.mycoludmusic.http.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by fanrui on 2020/12/10
 * Describe:自定义okHttp缓存策略( 有网时的缓存 )
 */
public class NetCacheInterceptor implements Interceptor {
    static final List<String> filterPaths = new ArrayList<>();

    static {
        filterPaths.add("/anti.s");
        filterPaths.add("/song/url");
        filterPaths.add("/user/playlist");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().url().getPath();
        Response response = chain.proceed(request);
        for (String s : filterPaths){
            if (path.contains(s)){
                return response;
            }
        }
        int onlineCacheTime = 60 * 60;//在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0(单位：秒)
        return response.newBuilder()
                .header("Cache-Control", "public, max-age=" + onlineCacheTime)
                .removeHeader("Pragma")
                .build();
    }
}
