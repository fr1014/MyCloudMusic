package com.fr1014.mycoludmusic.data;

import androidx.lifecycle.LiveData;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWNewSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.BaseResponse;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.Comment;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.DayRecommend;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.HomeBlock;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.mv.MVInfo;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.CollectPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.NetizensPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.RecommendPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchDefault;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchHotDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.SearchRecommend;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale.AlbumDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale.SongSale;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.Logout;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.UserEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLevelInfo;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLikeIdList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLikeList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLikeMusic;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.search.WYSearchDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.WYSongLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.WYUserPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYManagePlayList;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.data.source.http.HttpDataSource;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;
import com.fr1014.mycoludmusic.data.source.local.room.LocalDataSource;
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike;
import com.fr1014.mycoludmusic.http.api.KKWApiService;
import com.fr1014.mycoludmusic.http.api.KWYApiService;
import com.fr1014.mymvvm.base.BaseModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Query;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class DataRepository extends BaseModel implements HttpDataSource, LocalDataSource {

    private volatile static DataRepository instance = null;
    private HttpDataSource httpDataSource;
    private LocalDataSource localDataSource;
    public KKWApiService kkwApiService;
    public KWYApiService kwyApiService;

    private DataRepository(HttpDataSource httpDataSource, LocalDataSource localDataSource, KKWApiService kkwApiService, KWYApiService kwyApiService) {
        this.httpDataSource = httpDataSource;
        this.localDataSource = localDataSource;
        this.kkwApiService = kkwApiService;
        this.kwyApiService = kwyApiService;
    }

    public static DataRepository getInstance(HttpDataSource httpDataSource, LocalDataSource localDataSource, KKWApiService kkwApiService, KWYApiService kwyApiService) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(httpDataSource, localDataSource, kkwApiService, kwyApiService);
                }
            }
        }
        return instance;
    }

    @Override
    public Observable<ResponseBody> getSongCover(String coverPath) {
        return httpDataSource.getSongCover(coverPath);
    }

    @Override
    public Observable<MVInfo> getWYMVInfo(String id) {
        return httpDataSource.getWYMVInfo(id);
    }

    @Override
    public Single<Comment> getWYComment(int type, long id, int sortType, String cursor, int pageSize, int pageNo) {
        return httpDataSource.getWYComment(type, id, sortType, cursor, pageSize, pageNo);
    }

    @Override
    public Observable<CollectPlaylist> getWYCollectPlayList(int type, long id, String timestamp) {
        return httpDataSource.getWYCollectPlayList(type, id, timestamp);
    }

    @Override
    public Observable<AlbumDetail> getWYAlbumDetail(long id) {
        return httpDataSource.getWYAlbumDetail(id);
    }

    @Override
    public Observable<SongSale> getWYSongSaleList(String type, int albumType) {
        return httpDataSource.getWYSongSaleList(type, albumType);
    }

    @Override
    public Observable<DayRecommend> getWYDayRecommend() {
        return httpDataSource.getWYDayRecommend();
    }

    @Override
    public Observable<Logout> getWYLogout() {
        return httpDataSource.getWYLogout();
    }

    @Override
    public Observable<UserEntity> getUserAccountInfo() {
        return httpDataSource.getUserAccountInfo();
    }

    @Override
    public Observable<BaseResponse> getWYPhoneCaptcha(String phone, String captcha) {
        return httpDataSource.getWYPhoneCaptcha(phone, captcha);
    }

    @Override
    public Observable<BaseResponse> getWYPhoneVerify(String phone) {
        return httpDataSource.getWYPhoneVerify(phone);
    }

    @Override
    public Observable<UserEntity> getWYUserProfile(String phone, String password) {
        return httpDataSource.getWYUserProfile(phone, password);
    }

    @Override
    public Observable<WYLikeIdList> getWYLikeIdList(Long uid, String timestamp) {
        return httpDataSource.getWYLikeIdList(uid, timestamp);
    }

    @Override
    public Observable<WYLikeMusic> likeMusicWY(long id, boolean like) {
        return httpDataSource.likeMusicWY(id, like);
    }

    @Override
    public Observable<WYLikeList> getWYLikeList(long uid) {
        return httpDataSource.getWYLikeList(uid);
    }

    @Override
    public Observable<WYManagePlayList> getWYManagePlayList(String op, Long pid, String tracks, String timestamp) {
        return httpDataSource.getWYManagePlayList(op, pid, tracks, timestamp);
    }

    @Override
    public Observable<WYUserPlayList> getWYUserPlayList(long uid, String timestamp) {
        return httpDataSource.getWYUserPlayList(uid, timestamp);
    }

    @Override
    public Observable<WYLevelInfo> getWYLevelInfo() {
        return httpDataSource.getWYLevelInfo();
    }

    @Override
    public Observable<NetizensPlaylist> getWYNetizensPlayList(String order, String cat, int limit, int offset) {
        return httpDataSource.getWYNetizensPlayList(order, cat, limit, offset);
    }

    @Override
    public Observable<TopListEntity> getTopList() {
        return httpDataSource.getTopList();
    }

    @Override
    public Observable<TopListDetailEntity> getTopListDetail() {
        return httpDataSource.getTopListDetail();
    }

    @Override
    public Observable<PlayListDetailEntity> getPlayListDetail(long id) {
        return httpDataSource.getPlayListDetail(id);
    }

    @Override
    public Single<SongDetailEntity> getWYSongDetail(String ids) {
        return httpDataSource.getWYSongDetail(ids);
    }

    public Observable<SongUrlEntity> getWYSongUrl(String ids, String timestamp) {
        return httpDataSource.getWYSongUrl(ids, timestamp);
    }

    @Override
    public Observable<SearchHotDetail> getSearchHotDetail() {
        return httpDataSource.getSearchHotDetail();
    }

    @Override
    public Observable<SearchDefault> getSearchDefault(String timestamp) {
        return httpDataSource.getSearchDefault(timestamp);
    }

    @Override
    public Observable<SearchRecommend> getSearchMatch(String keywords, String type) {
        return httpDataSource.getSearchMatch(keywords, type);
    }

    @Override
    public Observable<CheckEntity> checkMusic(long id) {
        return httpDataSource.checkMusic(id);
    }

    @Override
    public Observable<WYSearchDetail> getWYSearch(String keywords, int offset) {
        return httpDataSource.getWYSearch(keywords, offset);
    }

    @Override
    public Observable<WYSongLrcEntity> getWYSongLrcEntity(String id) {
        return httpDataSource.getWYSongLrcEntity(id);
    }

    @Override
    public Observable<RecommendPlayList> getWYRecommendPlayList(int limit) {
        return httpDataSource.getWYRecommendPlayList(limit);
    }

    @Override
    public Observable<HomeBlock> getWYHomeBlock(boolean refresh, String timestamp) {
        return httpDataSource.getWYHomeBlock(refresh, timestamp);
    }

//    @Override
//    public Observable<KWSearchEntity> getSearch(String name, int page, int count) {
//        return httpDataSource.getSearch(name, page, count);
//    }

    public Observable<ResponseBody> getKWSongUrl(String rid) {
        return httpDataSource.getKWSongUrl(rid);
    }

    @Override
    public Observable<ResponseBody> getSearchResult(String name, int count) {
        return httpDataSource.getSearchResult(name, count);
    }

    @Override
    public Observable<KWNewSearchEntity> getKWSearchResult(String name, int page, int count) {
        return httpDataSource.getKWSearchResult(name, page, count);
    }

    @Override
    public Observable<KWSongDetailEntity> getKWSongDetail(long mid) {
        return httpDataSource.getKWSongDetail(mid);
    }

    @Override
    public Observable<KWSongInfoAndLrcEntity> getKWSongInfoAndLrc(String mid) {
        return httpDataSource.getKWSongInfoAndLrc(mid);
    }

    @Override
    public LiveData<List<MusicEntity>> getAllLive() {
        return localDataSource.getAllLive();
    }

    @Override
    public LiveData<List<MusicEntity>> getAllHistoryOrCurrentLive(boolean history) {
        return localDataSource.getAllHistoryOrCurrentLive(history);
    }

    @Override
    public LiveData<MusicEntity> getItemLive(String title, String artist) {
        return localDataSource.getItemLive(title, artist);
    }

    @Override
    public List<MusicEntity> getAll() {
        return localDataSource.getAll();
    }

    @Override
    public List<MusicEntity> getAllHistoryOrCurrent(boolean history) {
        return localDataSource.getAllHistoryOrCurrent(history);
    }

    @Override
    public MusicEntity getItem(String title, String artist, boolean isHistory) {
        return localDataSource.getItem(title, artist, isHistory);
    }

    @Override
    public void insertAll(List<MusicEntity> musicEntities) {
        localDataSource.insertAll(musicEntities);
    }

    @Override
    public void insert(MusicEntity musicEntity) {
        localDataSource.insert(musicEntity);
    }

    @Override
    public void delete(MusicEntity musicEntity) {
        localDataSource.delete(musicEntity);
    }

    @Override
    public void deleteAllMusicEntity() {
        localDataSource.deleteAllMusicEntity();
    }

    @Override
    public LiveData<List<MusicLike>> getLikeIdsLive() {
        return localDataSource.getLikeIdsLive();
    }

    @Override
    public List<MusicLike> getLikeIds() {
        return localDataSource.getLikeIds();
    }

    @Override
    public MusicLike getItemLive(String id) {
        return localDataSource.getItemLive(id);
    }

    @Override
    public void insert(MusicLike musicLike) {
        localDataSource.insert(musicLike);
    }

    @Override
    public void delete(MusicLike musicLike) {
        localDataSource.delete(musicLike);
    }

    @Override
    public void insertAllLikeIds(List<MusicLike> musicLikes) {
        localDataSource.insertAllLikeIds(musicLikes);
    }

    @Override
    public void deleteAllLikeIds() {
        localDataSource.deleteAllLikeIds();
    }
}
