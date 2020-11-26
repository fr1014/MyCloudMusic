package com.fr1014.mycoludmusic.data;

import com.fr1014.mycoludmusic.data.source.http.KWApiService;
import com.fr1014.mycoludmusic.data.source.http.WYApiService;
import com.fr1014.mycoludmusic.data.source.http.HttpDataSource;
import com.fr1014.mycoludmusic.entity.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.entity.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.entity.wangyiyun.SearchEntity;
import com.fr1014.mycoludmusic.entity.wangyiyun.SongDetailEntity;
import com.fr1014.mycoludmusic.entity.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.entity.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.entity.wangyiyun.TopListEntity;
import com.fr1014.mymvvm.base.BaseModel;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class DataRepository extends BaseModel implements HttpDataSource {

    private volatile static DataRepository instance = null;
    private WYApiService wyApiService;
    private KWApiService kwApiService;

    private DataRepository(WYApiService wyApiService,KWApiService kwApiService){
        this.wyApiService = wyApiService;
        this.kwApiService = kwApiService;
    }

    public static DataRepository getInstance(WYApiService apiService,KWApiService kwApiService){
        if (instance == null){
            synchronized (DataRepository.class){
                if (instance == null){
                    instance = new DataRepository(apiService,kwApiService);
                }
            }
        }
        return instance;
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
    public Observable<SongDetailEntity> getSongDetail(long ids) {
        return wyApiService.getSongDetail(ids);
    }

    @Override
    public Observable<SongUrlEntity> getSongUrl(long id) {
        return wyApiService.getSongUrl(id);
    }

    @Override
    public Observable<CheckEntity> checkMusic(long id) {
        return wyApiService.checkMusic(id);
    }

    @Override
    public Observable<SearchEntity> getSearch(String keywords,int offset) {
        return wyApiService.getSearch(keywords,offset);
    }

    @Override
    public Observable<com.fr1014.mycoludmusic.entity.kuwo.SearchEntity> getSearch(String name, int page, int count) {
        return kwApiService.getSearchEntity(name,page,count);
    }

    @Override
    public Observable<ResponseBody> getSongUrl(String rid) {
        return kwApiService.getSongUrl(rid);
    }
}
