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
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.http.WYYServiceProvider;
import com.fr1014.mycoludmusic.musicmanager.PlayService;
import com.fr1014.mymvvm.base.BaseApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
        Preferences.init(this); //SharePreferences
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);

        WYYServiceProvider.init(createWYYRetrofitBuilder());
        KWServiceProvider.init(createKWRetrofitBuilder());
    }

    private Retrofit.Builder createWYYRetrofitBuilder() {
        return new Retrofit.Builder()
                .client(createOkHttpClient())
                .addConverterFactory(LenientGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    private Retrofit.Builder createKWRetrofitBuilder() {
        return new Retrofit.Builder()
                .client(createOkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    private OkHttpClient createOkHttpClient(){
        try {
            return new OkHttpClient.Builder()
                    .sslSocketFactory(SSLUtils.getSSLSocketFactory())
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .addNetworkInterceptor(HttpLogger.getHttpLoggingInterceptor())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DataRepository provideRepository() {
        //WYY api
        WYApiService wyApiService = WYYServiceProvider.create(WYApiService.class);
        //KW api
        KWApiService kwApiService = KWServiceProvider.create(KWApiService.class);

        //数据仓库
        return DataRepository.getInstance(HttpDataSourceImpl.getInstance(wyApiService, kwApiService), LocalDataSourceImpl.getInstance());
    }


}
