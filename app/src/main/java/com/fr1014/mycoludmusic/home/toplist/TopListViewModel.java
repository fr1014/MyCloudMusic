package com.fr1014.mycoludmusic.home.toplist;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fr1014.musicmanager.Music;
import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.entity.CheckEntity;
import com.fr1014.mycoludmusic.entity.PlayListDetailEntity;
import com.fr1014.mycoludmusic.entity.SearchEntity;
import com.fr1014.mycoludmusic.entity.SongDetailEntity;
import com.fr1014.mycoludmusic.entity.SongUrlEntity;
import com.fr1014.mycoludmusic.entity.TopListDetailEntity;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mymvvm.base.BaseViewModel;
import com.fr1014.mymvvm.base.BusLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class TopListViewModel extends BaseViewModel<DataRepository> {

    private MutableLiveData<TopListDetailEntity> getTopListDetail;
    private BusLiveData<List<Music>> getPlayListDetail;
    private BusLiveData<Music> getSongUrl;
    private BusLiveData<List<Music>> getSearch;
    private BusLiveData<Boolean> getCheckSongResult;

    public TopListViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
    }

    public BusLiveData<Boolean> getCheckSongResult() {
        if (getCheckSongResult == null){
            getCheckSongResult = new BusLiveData<>();
        }
        return getCheckSongResult;
    }

    //搜索
    public BusLiveData<List<Music>> getSearch() {
        if (getSearch == null) {
            getSearch = new BusLiveData<>();
        }
        return getSearch;
    }

    public BusLiveData<Music> getSongUrl() {
        if (getSongUrl == null) {
            getSongUrl = new BusLiveData<>();
        }
        return getSongUrl;
    }

    public BusLiveData<List<Music>> getPlayListDetail(long id) {
        if (getPlayListDetail == null) {
            getPlayListDetail = new BusLiveData<>();
        }
        getPlayListDetailEntity(id);
        Log.d(TAG, "----getPlayListDetail: " + id);
        return getPlayListDetail;
    }

    public MutableLiveData<TopListDetailEntity> getTopListDetail() {
        if (getTopListDetail == null) {
            getTopListDetail = new MutableLiveData<>();
            getTopListDetailEntity();
        }
        return getTopListDetail;
    }

    private static final String TAG = "TopListViewModel";

    public void getSongUrlEntity(Music music) {
        model.getSongUrl(music.getId())
                .compose(RxSchedulers.apply())
                .map(new Function<SongUrlEntity, Music>() {
                    @Override
                    public Music apply(SongUrlEntity songUrlEntity) throws Exception {
                        music.setSongUrl(songUrlEntity.getData().get(0).getUrl());
                        return music;
                    }
                })
                .subscribe(new Observer<Music>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Music music) {
                        if (TextUtils.isEmpty(music.getSongUrl())) {
                            CommonUtil.toastShort("该歌曲暂不支持播放");
                        } else {
                            getSongDetailEntity(music);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //获取歌单详情
    private void getPlayListDetailEntity(final long id) {
        model.getPlayListDetail(id)
                .map(new Function<PlayListDetailEntity, List<Music>>() {
                    @Override
                    public List<Music> apply(PlayListDetailEntity playListDetailEntity) throws Exception {
                        List<Music> musics = new ArrayList<>();
                        List<PlayListDetailEntity.PlaylistBean.TracksBean> tracks = playListDetailEntity.getPlaylist().getTracks();
                        for (PlayListDetailEntity.PlaylistBean.TracksBean data : tracks) {
                            Music music = new Music();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < data.getAr().size(); i++) {
                                PlayListDetailEntity.PlaylistBean.TracksBean.ArBean ar = data.getAr().get(i);
                                if (i < data.getAr().size() - 1) {
                                    sb.append(ar.getName()).append('/');
                                } else {
                                    sb.append(ar.getName());
                                }
                            }
                            music.setId(data.getId());
                            music.setArtist(sb.toString());
                            music.setTitle(data.getName());
                            music.setImgUrl(data.getAl().getPicUrl());
                            musics.add(music);
                        }

                        return musics;
                    }
                })
                .compose(RxSchedulers.apply())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musics) {
                        getPlayListDetail.postValue(musics);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "----onError: getPlayListDetailEntity: " + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //获取榜单
    private void getTopListDetailEntity() {
        model.getTopListDetail()
                .compose(RxSchedulers.apply())
                .subscribe(new Observer<TopListDetailEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TopListDetailEntity topListDetailEntity) {
                        getTopListDetail.postValue(topListDetailEntity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "----onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //获取搜索结果
    public void getSearchEntity(String keywords, int offset) {
        model.getSearch(keywords, offset)
                .map(new Function<SearchEntity, List<Music>>() {
                    @Override
                    public List<Music> apply(SearchEntity searchEntity) throws Exception {
                        List<Music> musics = new ArrayList<>();
                        List<SearchEntity.ResultBean.SongsBean> songs = searchEntity.getResult().getSongs();
                        for (SearchEntity.ResultBean.SongsBean song : songs) {
                            Music music = new Music();
                            List<SearchEntity.ResultBean.SongsBean.ArtistsBean> artists = song.getArtists();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < artists.size(); i++) {
                                if (i < artists.size() - 1) {
                                    sb.append(artists.get(i).getName()).append("/");
                                } else {
                                    sb.append(artists.get(i).getName());
                                }
                            }
                            music.setArtist(sb.toString());
                            music.setTitle(song.getName());
                            music.setId(song.getId());
                            musics.add(music);
                        }
                        return musics;
                    }
                })
                .compose(RxSchedulers.apply())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musics) {
                        getSearch.postValue(musics);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "----onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //通过搜索得到的歌曲，需要通过获取歌曲详情来获取音乐专辑图片
    public void getSongDetailEntity(Music music) {
        model.getSongDetail(music.getId())
                .map(new Function<SongDetailEntity, Music>() {
                    @Override
                    public Music apply(SongDetailEntity songDetailEntity) throws Exception {
                        if (songDetailEntity.getSongs() != null && songDetailEntity.getSongs().size() > 0) {
                            music.setImgUrl(songDetailEntity.getSongs().get(0).getAl().getPicUrl());
                        }
                        return music;
                    }
                })
                .compose(RxSchedulers.apply())
                .subscribe(new Observer<Music>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Music music) {
                        getSongUrl.postValue(music);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //检索歌曲是否可以听
    public void checkSong(Music item) {
        model.checkMusic(item.getId())
                .compose(RxSchedulers.apply())
                .subscribe(new Observer<CheckEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CheckEntity checkEntity) {
                        if (checkEntity.isSuccess()) {
                            getSongUrlEntity(item);
                        } else {
                            CommonUtil.toastShort(checkEntity.getMessage());
                            //播放下一首
                            // TODO: 2020/9/17
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO: 2020/9/18
                        // retrofit2.adapter.rxjava2.HttpException: HTTP 404 Not Found
                        CommonUtil.toastShort("播放歌曲失败：" + e.getMessage());
                        Log.d(TAG, "------onError: " + e.toString());
//                        getCheckSongResult.postValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
