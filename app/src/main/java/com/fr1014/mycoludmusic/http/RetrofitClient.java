package com.fr1014.mycoludmusic.http;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;

    //http://101.133.238.38:3000/
    //http://149.129.123.124:3000/
    public static String baseUrl = "http://149.129.123.124:3000/";
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static RetrofitClient instance = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.instance;
    }

    private RetrofitClient() {
        this(baseUrl);
    }

    private RetrofitClient(String url) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(LenientGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    /**
     * create api service
     */
    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
