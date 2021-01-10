package com.fr1014.mycoludmusic.ui.vm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mymvvm.base.BaseViewModel;
import com.fr1014.mymvvm.base.BusLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class CommonViewModel extends BaseViewModel<DataRepository> {

    protected BusLiveData<List<Music>> getPlayListDetail;
    protected BusLiveData<Boolean> getStartPlayListDetail;
    private BusLiveData<Music> getSongUrl;

    public CommonViewModel(@NonNull Application application) {
        super(application);
    }

    public CommonViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
    }

    public BusLiveData<Music> getSongUrl() {
        if (getSongUrl == null) {
            getSongUrl = new BusLiveData<>();
        }
        return getSongUrl;
    }

    public BusLiveData<Boolean> getStartPlayListDetail() {
        if (getStartPlayListDetail == null){
            getStartPlayListDetail = new BusLiveData<>();
        }
        return getStartPlayListDetail;
    }

    public BusLiveData<List<Music>> getPlayListDetail(long id) {
        if (getPlayListDetail == null) {
            getPlayListDetail = new BusLiveData<>();
        }
        getPlayListDetailEntity(id);
        return getPlayListDetail;
    }

    //获取歌单详情(网易)
    private void getPlayListDetailEntity(final long id) {
        addSubscribe(model.getPlayListDetail(id)
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
                                    sb.append(ar.getName()).append('&');
                                } else {
                                    sb.append(ar.getName());
                                }
                            }
                            music.setId(data.getId());
                            music.setArtist(sb.toString());
                            music.setTitle(data.getName());
                            music.setImgUrl(data.getAl().getPicUrl());
                            music.setDuration(data.getDt());
                            music.setAlbum(data.getAl().getName());
                            musics.add(music);
                        }

                        return musics;
                    }
                })
                .compose(RxSchedulers.apply())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getStartPlayListDetail.setValue(true);
//                        showDialog();
                    }
                })
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musicList) throws Exception {
//                        dismissDialog();
                        getPlayListDetail.postValue(musicList);
                    }
                }));
    }

    /**
     * 酷我
     *
     * @param music
     */
    public void getKWSongUrl(Music music) {
        if (TextUtils.isEmpty(music.getMUSICRID())) return;

        addSubscribe(model.getKWSongUrl(music.getMUSICRID())
                .compose(RxSchedulers.applyIO())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        try {
                            String url = responseBody.string();
                            music.setSongUrl(url);
                            getKWSongDetail(music);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    public void getKWSongDetail(Music music) {
        try {
            long mid = Long.parseLong(music.getMUSICRID().replace("MUSIC_", ""));
            addSubscribe(model.getKWSongDetail(mid)
                    .compose(RxSchedulers.apply())
                    .subscribe(new Consumer<KWSongDetailEntity>() {
                        @Override
                        public void accept(KWSongDetailEntity kwSongDetailEntity) throws Exception {
                            music.setImgUrl(kwSongDetailEntity.getData().getAlbumpic());
                            music.setDuration(CommonUtil.stringToDuration(kwSongDetailEntity.getData().getSongTimeMinutes()));
                            getSongUrl().postValue(music);
                        }
                    }));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void getWYYSongUrl(Music music) {
        //可以调用addSubscribe()添加Disposable，请求与View周期同步
        addSubscribe(model.getWYSongUrl(music.getId())
                .compose(RxSchedulers.apply())
                .map(new Function<SongUrlEntity, Music>() {
                    @Override
                    public Music apply(SongUrlEntity songUrlEntity) throws Exception {
                        music.setSongUrl(songUrlEntity.getData().get(0).getUrl());
                        return music;
                    }
                })
                .subscribe(new Consumer<Music>() {
                    @Override
                    public void accept(Music music) throws Exception {
                        if (TextUtils.isEmpty(music.getSongUrl())) {
                            CommonUtil.toastShort("该歌曲暂不支持播放");
                        } else {
                            getSongDetailEntity(music);
                        }
                    }
                }));
    }

    //通过搜索得到的歌曲，需要通过获取歌曲详情来获取音乐专辑图片
    private void getSongDetailEntity(Music music) {
        addSubscribe(model.getSongDetail(music.getId())
                .map(songDetailEntity -> {
                    if (songDetailEntity.getSongs() != null && songDetailEntity.getSongs().size() > 0) {
                        music.setImgUrl(songDetailEntity.getSongs().get(0).getAl().getPicUrl());
                        music.setDuration(songDetailEntity.getSongs().get(0).getDt());
                    }
                    return music;
                })
                .compose(RxSchedulers.apply())
                .subscribe(new Consumer<Music>() {
                    @Override
                    public void accept(Music music) throws Exception {
                        getSongUrl().postValue(music);
                    }
                }));
    }
}
