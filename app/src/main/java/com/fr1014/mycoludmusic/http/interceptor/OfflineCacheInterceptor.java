package com.fr1014.mycoludmusic.http.interceptor;

import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by fanrui on 2020/12/10
 * Describe:自定义okHttp缓存策略( 没网时的缓存 )
 */
public class OfflineCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isNetworkAvailable(MyApplication.getInstance())) {
            int offlineCacheTime = 90;//离线的时候的缓存的过期时间
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                    .build();
        }
        return chain.proceed(request);
    }

}
