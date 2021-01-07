package com.fr1014.mycoludmusic.http.api;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSongLrcEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 * 网易API
 */
public interface WYApiService {

    //所有榜单
    @GET("toplist")
    Observable<TopListEntity> getTopList();

    //所有榜单内容摘要
    @GET("toplist/detail")
    Observable<TopListDetailEntity> getTopListDetail();

    @GET("top/list")
    Observable<PlayListDetailEntity> getTopList(@Query("id") long id);

    @GET("playlist/detail")
    Observable<PlayListDetailEntity> getPlayListDetail(@Query("id") long id);

    @GET("song/detail")
    Observable<SongDetailEntity> getSongDetail(@Query("ids") long ids);

    /**
     * 获取音乐url,可传入多个id用 "," 隔开
     * eg: /song/url?id=33894312  /song/url?id=405998841,33894312
     */
    @GET("song/url")
    Observable<SongUrlEntity> getWYSongUrl(@Query("id") String ids);

    @GET("song/url")
    Observable<SongUrlEntity> getWYSongUrl(@Query("id") long id);

    @GET("check/music")
    Observable<CheckEntity> checkMusic(@Query("id") long id);

    /**
     * 可选参数 :
     * limit : 返回数量 , 默认为 30
     * offset : 偏移数量，用于分页 , 如 : 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0
     * type: 搜索类型；默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV,
     * 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合
     *
     * @param keywords
     * @return
     */
//    @GET("/search")
//    Observable<WYSearchEntity> getSearch(@Query("keywords") String keywords, @Query("offset") int offset);

    @GET("cloudsearch")
    Observable<WYSearchDetail> getWYSearch(@Query("keywords") String keywords, @Query("offset") int offset);

    @GET("lyric")
    Observable<WYSongLrcEntity> getWYSongLrcEntity(@Query("id") long id);

//    /**
//     * KW的api 返回的为json
//     * @param name  需要搜索的歌曲或歌手
//     * @param page  查询的页码数
//     * @param count 当前页的返回数量
//     * @return SearchEntity
//     */
//    @GET("http://search.kuwo.cn/r.s?ft=music&itemset=web_2013&client=kt&rformat=json&encoding=utf8")
//    Observable<KWSearchEntity> getSearchEntity(@Query("all") String name, @Query("qn") int page, @Query("rn") int count);
}
