package com.fr1014.mycoludmusic.data.source.http;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWNewSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.BaseResponse;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.DayRecommend;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.HomeBlock;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchDefault;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchRecommend;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.NetizensPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.RecommendPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale.AlbumDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale.SongSale;
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
import okhttp3.ResponseBody;
import retrofit2.http.Query;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public interface HttpDataSource {
    /*
      ========================项目===================================
    */
    Observable<ResponseBody> getSongCover(String coverPath);

    /*
     ========================网易云===================================
   */
    Observable<AlbumDetail> getWYAlbumDetail(long id);

    Observable<SongSale> getWYSongSaleList(String type, int albumType);

    Observable<DayRecommend> getWYDayRecommend();

    Observable<Logout> getWYLogout();

    Observable<UserEntity> getUserAccountInfo();

    Observable<BaseResponse> getWYPhoneCaptcha(String phone, String captcha);

    Observable<BaseResponse> getWYPhoneVerify(String phone);

    Observable<UserEntity> getWYUserProfile(String phone, String password);

    Observable<WYLikeIdList> getWYLikeIdList(Long uid, String timestamp);

    Observable<WYLikeMusic> likeMusicWY(long id, boolean like);

    Observable<WYLikeList> getWYLikeList(long uid);

    Observable<WYManagePlayList> getWYManagePlayList(String op, Long pid, String tracks, String timestamp);

    Observable<WYUserPlayList> getWYUserPlayList(long uid);

    Observable<NetizensPlaylist> getWYNetizensPlayList(String order, String cat, int limit, int offset);

    Observable<WYLevelInfo> getWYLevelInfo();

    Observable<TopListEntity> getTopList();

    Observable<TopListDetailEntity> getTopListDetail();

    Observable<PlayListDetailEntity> getPlayListDetail(long id);

    Observable<SongDetailEntity> getWYSongDetail(String ids);

    Observable<CheckEntity> checkMusic(long id);

    Observable<SongUrlEntity> getWYSongUrl(String id, String timestamp);

    Observable<SearchHotDetail> getSearchHotDetail();

    Observable<SearchDefault> getSearchDefault(String timestamp);

    Observable<SearchRecommend> getSearchMatch(String keywords, String type);

    Observable<WYSearchDetail> getWYSearch(String keywords, int offset);

    Observable<WYSongLrcEntity> getWYSongLrcEntity(long id);

    Observable<RecommendPlayList> getWYRecommendPlayList(int limit);

    Observable<HomeBlock> getWYHomeBlock(boolean refresh, String timestamp);

    /*
    ===============================酷我========================================
    */
//    Observable<KWSearchEntity> getSearch(String name, int page, int count);
    Observable<ResponseBody> getKWSongUrl(String rid);

    Observable<ResponseBody> getSearchResult(String name, int count);

    Observable<KWNewSearchEntity> getKWSearchResult(String name, int page, int count);

    Observable<KWSongDetailEntity> getKWSongDetail(long mid);

    Observable<KWSongInfoAndLrcEntity> getKWSongInfoAndLrc(String mid);
}

