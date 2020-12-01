package com.fr1014.mycoludmusic.data.source.http;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.SearchEntity;

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
    @GET("开源版本省略api")
    Observable<SearchEntity> getSearchEntity(@Query("all")String name,@Query("qn")int page,@Query("rn")int count);

    @GET("开源版本省略api")
    Observable<ResponseBody> getSongUrl(@Query("rid")String rid);
}
