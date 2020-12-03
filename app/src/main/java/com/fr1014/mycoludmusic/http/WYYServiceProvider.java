package com.fr1014.mycoludmusic.http;

import android.text.TextUtils;

import com.fr1014.mycoludmusic.http.api.ApiConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class WYYServiceProvider {
    //超时时间
    public static final int DEFAULT_TIMEOUT = 20;
    //缓存时间
    public static final int CACHE_TIMEOUT = 10 * 1024 * 1024;

    private static Retrofit retrofit;

    public static void init(Retrofit.Builder builder) {
        if (retrofit == null) {
            builder.baseUrl(ApiConstants.BASE_URL_WYY);
            retrofit = builder.build();
        }
    }

    public static <T> T create(final Class<T> service) {
        try {
            return retrofit.create(service);
        } catch (Exception e) {
            throw new RuntimeException("Create service Exception by class!");
        }
    }
}
