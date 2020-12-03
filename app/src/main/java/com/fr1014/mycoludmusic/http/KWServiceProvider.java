package com.fr1014.mycoludmusic.http;

import com.fr1014.mycoludmusic.http.api.ApiConstants;

import retrofit2.Retrofit;

public class KWServiceProvider {
    //缓存设置
    public static final String IS_FRESH = "refresh";
    public static final String CACHE_TIME = "cache_time";
    public static final int DEFAULT_CACHE_TIME = 30 * 60;

    private static Retrofit retrofit;

    public static void init(Retrofit.Builder builder) {
        if (retrofit == null) {
            builder.baseUrl(ApiConstants.BASE_URL_KW);
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
