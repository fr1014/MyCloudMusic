package com.fr1014.mycoludmusic.ui.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mymvvm.base.BaseViewModel;
import com.fr1014.mymvvm.base.BusLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class CommonViewModel extends BaseViewModel<DataRepository> {

    protected BusLiveData<List<Music>> getPlayListDetail;
    protected BusLiveData<Boolean> getStartPlayListDetail;

    public CommonViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
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
}
