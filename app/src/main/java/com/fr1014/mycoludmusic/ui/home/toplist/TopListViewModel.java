package com.fr1014.mycoludmusic.ui.home.toplist;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.WYSongLrcEntity;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.MyDisposableObserver;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mycoludmusic.utils.FileUtils;
import com.fr1014.mymvvm.base.BusLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class TopListViewModel extends CommonViewModel {

    private MutableLiveData<TopListDetailEntity> getTopListDetail;
    private BusLiveData<List<Music>> getSongListUrl;

    public TopListViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
    }

    public BusLiveData<List<Music>> getSongListUrl(List<Music> musicList) {
        if (getSongListUrl == null) {
            getSongListUrl = new BusLiveData<>();
        }
        getWYYSongUrl(musicList);
        return getSongListUrl;
    }

    public MutableLiveData<TopListDetailEntity> getTopListDetail() {
        if (getTopListDetail == null) {
            getTopListDetail = new MutableLiveData<>();
            getTopListDetailEntity();
        }
        return getTopListDetail;
    }

    /**
     * @param musicList 需要获取url的歌曲集合
     */
    private void getWYYSongUrl(List<Music> musicList) {
        StringBuilder ids = new StringBuilder();
        for (int index = 0; index < musicList.size(); index++) {
            ids.append(musicList.get(index).getId());
            if (index != musicList.size() - 1) {
                ids.append(",");
            }
        }
        addSubscribe(model.getWYSongUrl(ids.toString())
                .compose(RxSchedulers.apply())
                .map(new Function<SongUrlEntity, List<Music>>() {
                    @Override
                    public List<Music> apply(SongUrlEntity songUrlEntity) throws Exception {
                        List<SongUrlEntity.DataBean> data = songUrlEntity.getData();
                        for (int index = 0; index < data.size(); index++) {
                            for (int mIndex = 0; mIndex < musicList.size(); mIndex++) {
                                Music music = musicList.get(mIndex);
                                if (TextUtils.isEmpty(music.getSongUrl())) {
                                    SongUrlEntity.DataBean dataBean = data.get(index);
                                    if (dataBean.getId() == music.getId()) {
                                        String url = data.get(index).getUrl();
                                        if (TextUtils.isEmpty(url)) {
                                            musicList.remove(music);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        return musicList;
                    }
                })
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musicList) throws Exception {
                        getSongListUrl.setValue(musicList);
                    }
                }));
    }

    //获取榜单（网易）
    private void getTopListDetailEntity() {
        addSubscribe(model.getTopListDetail()
                .compose(RxSchedulers.apply())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<TopListDetailEntity>() {
                    @Override
                    public void accept(TopListDetailEntity topListDetailEntity) throws Exception {
                        dismissDialog();
                        getTopListDetail.postValue(topListDetailEntity);
                    }
                }));
    }

//    public void insertMusicList(List<Music> musicList){
//        List<MusicEntity> musicEntities = new ArrayList<>();
//        for (Music music : musicList){
//            MusicEntity musicEntity = new MusicEntity(music.getTitle(),music.getArtist(),music.getImgUrl(),music.getId(),music.getMUSICRID(),music.getDuration(),false);
//            musicEntities.add(musicEntity);
//        }
//        model.insertAll(musicEntities);
//    }
//
//    public void deleteOldMusicList(){
//        Observable.just("")
//                .compose(RxSchedulers.applyIO())
//                .subscribe(new MyDisposableObserver<String>(){
//                    @Override
//                    public void onNext(@io.reactivex.annotations.NonNull String s) {
//                        List<MusicEntity> allOldCurrentMusic = model.getAllHistoryOrCurrent(false);
//                        for (MusicEntity musicEntity : allOldCurrentMusic){
//                            model.delete(musicEntity);
//                        }
//                    }
//                });
//    }
}
