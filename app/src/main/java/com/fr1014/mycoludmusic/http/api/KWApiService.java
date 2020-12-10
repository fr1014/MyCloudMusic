package com.fr1014.mycoludmusic.http.api;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * 酷我API
 */
public interface KWApiService {

    //返回的为String
    @GET("http://antiserver.kuwo.cn/anti.s?response=url&format=mp3&type=convert_url")
    Observable<ResponseBody> getKWSongUrl(@Query("rid") String rid);

    @GET("http://search.kuwo.cn/r.s?ft=music&rformat=json&encoding=utf8&callback=song&vipver=MUSIC_8.0.3.1")
    Observable<ResponseBody> getSearchResult(@Query("all") String name,@Query("rn")int count);

    @Headers({"Cookie: kw_token=KJUODZNUE5D","csrf: KJUODZNUE5D"})
    @GET("https://www.kuwo.cn/api/www/music/musicInfo?httpsStatus=1&reqId=0897b5c0-3888-11eb-8303-7909fb8ff0f2")
    Observable<KWSongDetailEntity> getKWSongDetail(@Query("mid")long mid);

    @GET("http://m.kuwo.cn/newh5/singles/songinfoandlrc")
    Observable<KWSongInfoAndLrcEntity> getKWSongInfoAndLrc(@Query("musicId")String mid);
}
