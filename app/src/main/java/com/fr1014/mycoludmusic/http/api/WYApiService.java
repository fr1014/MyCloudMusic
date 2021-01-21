package com.fr1014.mycoludmusic.http.api;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.NetizensPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.RecommendPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.UserEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLevelInfo;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLikeList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLikeMusic;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSongLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.WYUserPlayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 * 网易API
 */
public interface WYApiService {

    @POST("login/cellphone")
    Observable<UserEntity> getWYUserProfile(@Query("phone") String phone, @Query("password") String password);

    /*
    说明 : 调用此接口 , 传入音乐 id, 可喜欢该音乐
    必选参数 : id: 歌曲 id
    可选参数 : like: 布尔值 , 默认为 true 即喜欢 , 若传 false, 则取消喜欢
     */
    @GET("like")
    Observable<WYLikeMusic> likeMusicWY(@Query("id") long id, @Query("like") boolean like);

    //喜欢音乐列表
    @GET("likelist")
    Observable<WYLikeList> getWYLikeList(@Query("uid") long uid);

    @GET("user/playlist")
    Observable<WYUserPlayList> getWYUserPlayList(@Query("uid")long uid);

    /**
     * 说明 : 调用此接口 , 可获取网友精选碟歌单
     *
     * 可选参数 : order: 可选值为 'new' 和 'hot', 分别对应最新和最热 , 默认为 'hot'
     *
     * cat:cat: tag, 比如 " 华语 "、" 古风 " 、" 欧美 "、" 流行 ", 默认为 "全部",可从歌单分类接口获取(/playlist/catlist)
     *
     * limit: 取出歌单数量 , 默认为 50
     *
     * offset: 偏移数量 , 用于分页 , 如 :( 评论页数 -1)*50, 其中 50 为 limit 的值
     *
     * 接口地址 : /top/playlist
     *
     * 调用例子 : /top/playlist?limit=10&order=new
     * @return
     */
    @GET("top/playlist")
    Observable<NetizensPlaylist> getWYNetizensPlaylist(@Query("order") String order, @Query("cat") String cat, @Query("limit") int limit, @Query("offset") int offset);

    @GET("user/level")
    Observable<WYLevelInfo> getWYLevelInfo();

    //所有榜单
    @GET("toplist")
    Observable<TopListEntity> getTopList();

    //所有榜单内容摘要
    @GET("toplist/detail")
    Observable<TopListDetailEntity> getTopListDetail();

    @GET("playlist/detail")
    Observable<PlayListDetailEntity> getPlayListDetail(@Query("id") long id);

    @GET("song/detail")
    Observable<SongDetailEntity> getWYSongDetail(@Query("ids") String ids);

    /**
     * 获取音乐url,可传入多个id用 "," 隔开
     * eg: /song/url?id=33894312  /song/url?id=405998841,33894312
     */
    @GET("song/url")
    Observable<SongUrlEntity> getWYSongUrl(@Query("id") String ids);

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

    @GET("personalized")
    Observable<RecommendPlayList> getWYRecommendPlayList(@Query("limit") int limit);

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