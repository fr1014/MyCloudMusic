package com.fr1014.mycoludmusic.http.api;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.DayRecommend;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.HomeBlock;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchDefault;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchRecommend;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.NetizensPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.RecommendPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.Logout;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.UserEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLevelInfo;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLikeIdList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLikeList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLikeMusic;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.WYSearchDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.WYSongLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.WYUserPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYManagePlayList;

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

    /**
     * @return 获取每日推荐歌曲
     */
    @GET("recommend/songs")
    Observable<DayRecommend> getWYDayRecommend();

    @GET("logout")
    Observable<Logout> getWYLogout();

    @POST("login/cellphone")
    Observable<UserEntity> getWYUserProfile(@Query("phone") String phone, @Query("password") String password);

    @GET("likelist")
    Observable<WYLikeIdList> getWYLikeIdList(@Query("uid") Long uid, @Query("timestamp") String timestamp);

    /*
    说明 : 调用此接口 , 传入音乐 id, 可喜欢该音乐
    必选参数 : id: 歌曲 id
    可选参数 : like: 布尔值 , 默认为 true 即喜欢 , 若传 false, 则取消喜欢
     */
    @GET("like")
    Observable<WYLikeMusic> likeMusicWY(@Query("id") long id, @Query("like") boolean like);

    /*
      说明 : 调用此接口 , 可以添加歌曲到歌单或者从歌单删除某首歌曲 ( 需要登录 )
      必选参数 :
      op: 从歌单增加单曲为 add, 删除为 del
      pid: 歌单 id tracks: 歌曲 id,可多个,用逗号隔开
      时间戳： &timestamp=1503019930000
     */
    @GET("playlist/tracks")
    Observable<WYManagePlayList> getWYManagePlayList(@Query("op") String op, @Query("pid") Long pid, @Query("tracks") String tracks, @Query("timestamp") String timestamp);

    //喜欢音乐列表
    @GET("likelist")
    Observable<WYLikeList> getWYLikeList(@Query("uid") long uid);

    @GET("user/playlist")
    Observable<WYUserPlayList> getWYUserPlayList(@Query("uid") long uid);

    /**
     * 说明 : 调用此接口 , 可获取网友精选碟歌单
     * <p>
     * 可选参数 : order: 可选值为 'new' 和 'hot', 分别对应最新和最热 , 默认为 'hot'
     * <p>
     * cat:cat: tag, 比如 " 华语 "、" 古风 " 、" 欧美 "、" 流行 ", 默认为 "全部",可从歌单分类接口获取(/playlist/catlist)
     * <p>
     * limit: 取出歌单数量 , 默认为 50
     * <p>
     * offset: 偏移数量 , 用于分页 , 如 :( 评论页数 -1)*50, 其中 50 为 limit 的值
     * <p>
     * 接口地址 : /top/playlist
     * <p>
     * 调用例子 : /top/playlist?limit=10&order=new
     *
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
    Observable<SongUrlEntity> getWYSongUrl(@Query("id") String ids, @Query("timestamp") String timestamp);

    @GET("check/music")
    Observable<CheckEntity> checkMusic(@Query("id") long id);

    @GET("search/hot/detail")
    Observable<SearchHotDetail> getSearchHotDetail();

    @GET("search/default")
    Observable<SearchDefault> getSearchDefault(@Query("timestamp") String timestamp);

    /**
     * @param keywords 关键词
     * @param type 如果传 'mobile' 则返回移动端数据
     * @return 搜索建议
     */
    @GET("search/suggest")
    Observable<SearchRecommend> getSearchMatch(@Query("keywords")String keywords, @Query("type")String type);
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

    @GET("homepage/block/page")
    Observable<HomeBlock> getWYHomeBlock();

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