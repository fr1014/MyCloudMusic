package com.fr1014.mycoludmusic.data.source.http;

import com.fr1014.mycoludmusic.entity.kuwo.SearchEntity;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 酷我API
 */
public interface KWApiService {

    /**
     *
     * @param name 需要搜索的歌曲或歌手
     * @param page 查询的页码数
     * @param count 当前页的返回数量
     * @return SearchEntity
     */
    @GET("http://search.kuwo.cn/r.s?ft=music&itemset=web_2013&client=kt&rformat=json&encoding=utf8")
    Observable<SearchEntity> getSearchEntity(@Query("all")String name,@Query("qn")int page,@Query("rn")int count);

    @GET("http://antiserver.kuwo.cn/anti.s?type=convert_url&format=aac|mp3&response=url")
    Observable<ResponseBody> getSongUrl(@Query("rid")String rid);
}
