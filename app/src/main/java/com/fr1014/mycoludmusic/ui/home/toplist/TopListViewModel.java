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
    private BusLiveData<String> getSongLrcPath;

    public TopListViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
    }

    public BusLiveData<String> getSongLrcPath() {
        if (getSongLrcPath == null) {
            getSongLrcPath = new BusLiveData<>();
        }
        return getSongLrcPath;
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

    public void getSongLrc(Music music) {
        String filePath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
        if (!FileUtils.isFileEmpty(filePath)) {
            getSongLrcPath().setValue(filePath);
            return;
        }
        if (!TextUtils.isEmpty(music.getMUSICRID())) {
            String mid = music.getMUSICRID().replace("MUSIC_", "");
            addSubscribe(model.getKWSongInfoAndLrc(mid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(new Function<KWSongInfoAndLrcEntity, String>() {
                        @Override
                        public String apply(@io.reactivex.annotations.NonNull KWSongInfoAndLrcEntity kwSongInfoAndLrcEntity) throws Exception {
                            List<KWSongInfoAndLrcEntity.DataBean.LrclistBean> lrcList = kwSongInfoAndLrcEntity.getData().getLrclist();
                            StringBuilder content = new StringBuilder();
                            //正则格式化KW服务器返回的歌词时间
                            String rgex = "\\d+\\.\\d{1,3}";
                            Pattern pattern = Pattern.compile(rgex);
                            for (KWSongInfoAndLrcEntity.DataBean.LrclistBean lrcListBean : lrcList) {
                                content.append("[");
                                String time = lrcListBean.getTime();
                                Matcher m = pattern.matcher(time);
                                if (m.matches()) {
                                    content.append(CommonUtil.strFormatTime(m.group()));
                                } else {   //如果正则匹配失败，取.前和.后2位的字符
                                    int index = time.indexOf(".");
                                    int endIndex = index + 2;
                                    content.append(CommonUtil.strFormatTime(time.substring(0, endIndex)));
                                }
                                content.append("]");
                                content.append(lrcListBean.getLineLyric().trim());
                                content.append("\n");
                            }
                            FileUtils.saveLrcFile(filePath, content.toString());
                            return filePath;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String filePath) throws Exception {
                            getSongLrcPath().setValue(filePath);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            getSongLrcPath().setValue("");
                        }
                    })
            );
        } else {
            addSubscribe(model.getWYSongLrcEntity(music.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(new Function<WYSongLrcEntity, String>() {
                        @Override
                        public String apply(@io.reactivex.annotations.NonNull WYSongLrcEntity wySongLrcEntity) throws Exception {
                            String lyric = wySongLrcEntity.getLrc().getLyric();
                            FileUtils.saveLrcFile(filePath, lyric);
                            return filePath;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            getSongLrcPath().setValue(filePath);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            getSongLrcPath().setValue("");
                        }
                    })
            );
        }
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
