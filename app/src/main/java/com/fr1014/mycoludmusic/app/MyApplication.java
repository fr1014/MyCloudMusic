package com.fr1014.mycoludmusic.app;

import android.content.Intent;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.source.HttpDataSourceImpl;
import com.fr1014.mycoludmusic.data.source.LocalDataSourceImpl;
import com.fr1014.mycoludmusic.http.HttpLogger;
import com.fr1014.mycoludmusic.http.KWServiceProvider;
import com.fr1014.mycoludmusic.http.LenientGsonConverterFactory;
import com.fr1014.mycoludmusic.http.SSLUtils;
import com.fr1014.mycoludmusic.http.api.KWApiService;
import com.fr1014.mycoludmusic.http.api.WYApiService;
import com.fr1014.mycoludmusic.http.interceptor.AddCookiesInterceptor;
import com.fr1014.mycoludmusic.http.interceptor.NetCacheInterceptor;
import com.fr1014.mycoludmusic.http.interceptor.OfflineCacheInterceptor;
import com.fr1014.mycoludmusic.http.interceptor.ReceivedCookiesInterceptor;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.http.WYYServiceProvider;
import com.fr1014.mycoludmusic.musicmanager.PlayService;
import com.fr1014.mycoludmusic.rx.RxJavaError;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.fr1014.mymvvm.base.BaseApplication;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.fr1014.mycoludmusic.http.WYYServiceProvider.DEFAULT_TIMEOUT;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.get().init(this);
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);

        WYYServiceProvider.init(createWYYRetrofitBuilder());
        KWServiceProvider.init(createKWRetrofitBuilder());
        RxJavaError.setRxJavaErrorHandler();
    }

    private Retrofit.Builder createWYYRetrofitBuilder() {
        return new Retrofit.Builder()
                .client(createWYYOkHttpClient())
                .addConverterFactory(LenientGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    private Retrofit.Builder createKWRetrofitBuilder() {
        return new Retrofit.Builder()
                .client(createKWOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    private OkHttpClient createWYYOkHttpClient() {
        try {
            //setup cache
            File httpCacheDirectory = new File(getCacheDir(), "okHttpCache");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            return new OkHttpClient.Builder()
                    .addNetworkInterceptor(new NetCacheInterceptor())
                    .addInterceptor(new OfflineCacheInterceptor())
                    .addInterceptor(new AddCookiesInterceptor()) //添加Cookie
                    .addInterceptor(new ReceivedCookiesInterceptor()) //拦截Cookie
                    .cache(cache)
//                    .sslSocketFactory(SSLUtils.getSSLSocketFactory())
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .addNetworkInterceptor(HttpLogger.getHttpLoggingInterceptor())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private OkHttpClient createKWOkHttpClient() {
        //setup cache
        File httpCacheDirectory = new File(getCacheDir(), "okHttpCache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new NetCacheInterceptor())
                .addInterceptor(new OfflineCacheInterceptor())
                .cache(cache)
//                    .sslSocketFactory(SSLUtils.getSSLSocketFactory())
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(HttpLogger.getHttpLoggingInterceptor())
                .build();
    }

//    private OkHttpClient createKWOkHttpClient(){
//        try {
//            return new OkHttpClient.Builder()
//                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                    .addNetworkInterceptor(HttpLogger.getHttpLoggingInterceptor())
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static DataRepository provideRepository() {
        //WYY api
        WYApiService wyApiService = WYYServiceProvider.create(WYApiService.class);
        //KW api
        KWApiService kwApiService = KWServiceProvider.create(KWApiService.class);

        //数据仓库
        return DataRepository.getInstance(HttpDataSourceImpl.getInstance(wyApiService, kwApiService), LocalDataSourceImpl.getInstance());
    }


}
