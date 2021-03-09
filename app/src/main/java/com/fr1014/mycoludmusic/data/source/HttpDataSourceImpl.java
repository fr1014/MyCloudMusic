package com.fr1014.mycoludmusic.data.source;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWNewSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.DayRecommend;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.HomeBlock;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchDefault;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchRecommend;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.NetizensPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.RecommendPlayList;
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
import com.fr1014.mycoludmusic.data.source.http.HttpDataSource;
import com.fr1014.mycoludmusic.http.api.KWApiService;
import com.fr1014.mycoludmusic.http.api.WYApiService;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class HttpDataSourceImpl implements HttpDataSource {
    private volatile static HttpDataSourceImpl instance = null;
    private WYApiService wyApiService;
    private KWApiService kwApiService;

    public static HttpDataSourceImpl getInstance(WYApiService wyApiService, KWApiService kwApiService) {
        if (instance == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (instance == null) {
                    instance = new HttpDataSourceImpl(wyApiService, kwApiService);
                }
            }
        }
        return instance;
    }

    private HttpDataSourceImpl(WYApiService wyApiService, KWApiService kwApiService) {
        this.wyApiService = wyApiService;
        this.kwApiService = kwApiService;
    }

    @Override
    public Observable<ResponseBody> getSongCover(String coverPath) {
        return kwApiService.getSongCover(coverPath);
    }

    @Override
    public Observable<SongSale> getWYSongSaleList(String type, int albumType) {
        return wyApiService.getWYSongSaleList(type, albumType);
    }

    @Override
    public Observable<DayRecommend> getWYDayRecommend() {
        return wyApiService.getWYDayRecommend();
    }

    @Override
    public Observable<Logout> getWYLogout() {
        return wyApiService.getWYLogout();
    }

    @Override
    public Observable<UserEntity> getWYUserProfile(String phone, String password) {
        return wyApiService.getWYUserProfile(phone, password);
    }

    @Override
    public Observable<WYLikeIdList> getWYLikeIdList(Long uid,String timestamp) {
        return wyApiService.getWYLikeIdList(uid,timestamp);
    }

    @Override
    public Observable<WYLikeMusic> likeMusicWY(long id, boolean like) {
        return wyApiService.likeMusicWY(id, like);
    }

    @Override
    public Observable<WYLikeList> getWYLikeList(long uid) {
        return wyApiService.getWYLikeList(uid);
    }

    @Override
    public Observable<WYManagePlayList> getWYManagePlayList(String op, Long pid, String tracks, String timestamp) {
        return wyApiService.getWYManagePlayList(op, pid, tracks, timestamp);
    }

    @Override
    public Observable<WYUserPlayList> getWYUserPlayList(long uid) {
        return wyApiService.getWYUserPlayList(uid);
    }

    @Override
    public Observable<NetizensPlaylist> getWYNetizensPlayList(String order, String cat, int limit, int offset) {
        return wyApiService.getWYNetizensPlaylist(order, cat, limit, offset);
    }

    @Override
    public Observable<WYLevelInfo> getWYLevelInfo() {
        return wyApiService.getWYLevelInfo();
    }

    @Override
    public Observable<TopListEntity> getTopList() {
        return wyApiService.getTopList();
    }

    @Override
    public Observable<TopListDetailEntity> getTopListDetail() {
        return wyApiService.getTopListDetail();
    }

    @Override
    public Observable<PlayListDetailEntity> getPlayListDetail(long id) {
        return wyApiService.getPlayListDetail(id);
    }

    @Override
    public Observable<SongDetailEntity> getWYSongDetail(String ids) {
        return wyApiService.getWYSongDetail(ids);
    }

    @Override
    public Observable<CheckEntity> checkMusic(long id) {
        return wyApiService.checkMusic(id);
    }

    @Override
    public Observable<SongUrlEntity> getWYSongUrl(String ids, String timestamp) {
        return wyApiService.getWYSongUrl(ids, timestamp);
    }

    @Override
    public Observable<SearchHotDetail> getSearchHotDetail() {
        return wyApiService.getSearchHotDetail();
    }

    @Override
    public Observable<SearchDefault> getSearchDefault(String timestamp) {
        return wyApiService.getSearchDefault(timestamp);
    }

    @Override
    public Observable<SearchRecommend> getSearchMatch(String keywords, String type) {
        return wyApiService.getSearchMatch(keywords, type);
    }

    @Override
    public Observable<WYSearchDetail> getWYSearch(String keywords, int offset) {
        return wyApiService.getWYSearch(keywords, offset);
    }

    @Override
    public Observable<WYSongLrcEntity> getWYSongLrcEntity(long id) {
        return wyApiService.getWYSongLrcEntity(id);
    }

    @Override
    public Observable<RecommendPlayList> getWYRecommendPlayList(int limit) {
        return wyApiService.getWYRecommendPlayList(limit);
    }

    @Override
    public Observable<HomeBlock> getWYHomeBlock() {
        return wyApiService.getWYHomeBlock();
    }

//    @Override
//    public Observable<KWSearchEntity> getSearch(String name, int page, int count) {
//        return wyApiService.getSearchEntity(name, page, count);
//    }

    @Override
    public Observable<ResponseBody> getKWSongUrl(String rid) {
        return kwApiService.getKWSongUrl(rid);
    }

    @Override
    public Observable<ResponseBody> getSearchResult(String name, int count) {
        return kwApiService.getSearchResult(name, count);
    }

    @Override
    public Observable<KWNewSearchEntity> getKWSearchResult(String name, int page, int count) {
        return kwApiService.getKWSearchResult(name, page, count);
    }

    @Override
    public Observable<KWSongDetailEntity> getKWSongDetail(long mid) {
        return kwApiService.getKWSongDetail(mid);
    }

    @Override
    public Observable<KWSongInfoAndLrcEntity> getKWSongInfoAndLrc(String mid) {
        return kwApiService.getKWSongInfoAndLrc(mid);
    }
}
