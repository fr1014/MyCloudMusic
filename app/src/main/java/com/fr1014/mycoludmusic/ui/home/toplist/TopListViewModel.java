package com.fr1014.mycoludmusic.ui.home.toplist;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWNewSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSongLrcEntity;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mycoludmusic.utils.FileUtils;
import com.fr1014.mymvvm.base.BaseViewModel;
import com.fr1014.mymvvm.base.BusLiveData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class TopListViewModel extends BaseViewModel<DataRepository> {

    private MutableLiveData<TopListDetailEntity> getTopListDetail;
    private BusLiveData<List<Music>> getPlayListDetail;
    private BusLiveData<Music> getSongUrl;
    private BusLiveData<List<Music>> getSongListUrl;
    private BusLiveData<List<Music>> getSearch;
    private BusLiveData<Boolean> getCheckSongResult;
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

    public BusLiveData<Boolean> getCheckSongResult() {
        if (getCheckSongResult == null) {
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

    public BusLiveData<List<Music>> getSongListUrl(List<Music> musicList) {
        if (getSongListUrl == null) {
            getSongListUrl = new BusLiveData<>();
        }
        getSongUrlEntity(musicList);
        return getSongListUrl;
    }

    public BusLiveData<List<Music>> getPlayListDetail(long id) {
        if (getPlayListDetail == null) {
            getPlayListDetail = new BusLiveData<>();
        }
        getPlayListDetailEntity(id);
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

    /**
     * @param musicList 需要获取url的歌曲集合
     */
    private void getSongUrlEntity(List<Music> musicList) {
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

    //获取排行榜歌单详情(网易)
    private void getPlayListDetailEntity(final long id) {
        addSubscribe(model.getTopList(id)
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
                            music.setDuration(data.getDt());
                            musics.add(music);
                        }

                        return musics;
                    }
                })
                .compose(RxSchedulers.apply())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musicList) throws Exception {
                        dismissDialog();
                        getPlayListDetail.postValue(musicList);
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

    //获取搜索结果（网易）
    public void getSearchEntityWYY(String keywords, int offset) {
        addSubscribe(model.getSearch(keywords, offset)
                .map(new Function<WYSearchEntity, List<Music>>() {
                    @Override
                    public List<Music> apply(WYSearchEntity searchEntity) throws Exception {
                        List<Music> musics = new ArrayList<>();
                        List<WYSearchEntity.ResultBean.SongsBean> songs = searchEntity.getResult().getSongs();
                        for (WYSearchEntity.ResultBean.SongsBean song : songs) {
                            Music music = new Music();
                            List<WYSearchEntity.ResultBean.SongsBean.ArtistsBean> artists = song.getArtists();
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
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musicList) throws Exception {
                        getSearch.postValue(musicList);
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

    //检索歌曲是否可以听
    public void checkSong(Music item) {
        addSubscribe(model.checkMusic(item.getId())
                .compose(RxSchedulers.apply())
                .subscribe(new Consumer<CheckEntity>() {
                    @Override
                    public void accept(CheckEntity checkEntity) throws Exception {
                        if (checkEntity.isSuccess()) {
                            getSongUrlEntity(item);
                        } else {
                            CommonUtil.toastLong(item.getTitle() + " (无法播放：已播放其它歌曲)");
                            //播放下一首
                            getCheckSongResult.postValue(false);
                        }
                    }
                }));
    }

    //获取搜索结果（酷我）
    public void getSearchEntityKW(String name, int count) {
        addSubscribe(model.getSearchResult(name, count)
                .map(new Function<ResponseBody, List<Music>>() {
                    @Override
                    public List<Music> apply(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        String replace1 = result.replace("try{var jsondata=", "");
                        String replace2 = replace1.replace("; song(jsondata);}catch(e){jsonError(e)}", "");
                        String json = replace2.replaceAll("'", "\"");
                        Gson gson = new Gson();
                        KWSearchEntity searchEntity = gson.fromJson(json, KWSearchEntity.class);
                        List<Music> musics = new ArrayList<>();
                        List<KWSearchEntity.AbslistBean> abslistBeanList = searchEntity.getAbslist();
                        for (KWSearchEntity.AbslistBean abslistBean : abslistBeanList) {
                            Music music = new Music();
                            if (!TextUtils.isEmpty(abslistBean.getARTIST())) {
//                                music.setArtist(abslistBean.getARTIST());
                                music.setArtist(abslistBean.getARTIST().replaceAll("&nbsp;", " ").replaceAll("###", "/"));
                            } else {
                                music.setArtist(abslistBean.getAARTIST().replaceAll("&nbsp;", " ").replaceAll("###", "/"));
                            }
                            music.setTitle(abslistBean.getSONGNAME().replaceAll("&nbsp;", " "));
                            music.setImgUrl(abslistBean.getHts_MVPIC());
                            music.setMUSICRID(abslistBean.getMUSICRID());
                            musics.add(music);
                        }
                        return musics;
                    }
                }).compose(RxSchedulers.apply())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        showDialog();
                    }
                })
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musicList) throws Exception {
//                        dismissDialog();
                        getSearch().postValue(musicList);
                    }
                }));

    }

    //获取搜索结果（酷我）新的接口
    public void getSearchEntityKW(String name, int page, int count) {
        addSubscribe(model.getKWSearchResult(name, page, count)
                .map(new Function<KWNewSearchEntity, List<Music>>() {
                    @Override
                    public List<Music> apply(@io.reactivex.annotations.NonNull KWNewSearchEntity kwNewSearchEntity) throws Exception {
                        List<KWNewSearchEntity.AbslistBean> list = kwNewSearchEntity.getAbslist();
                        List<Music> musicList = new ArrayList<>();
                        for (KWNewSearchEntity.AbslistBean bean : list) {
                            Music music = new Music();
                            music.setMUSICRID(bean.getMUSICRID());
                            music.setTitle(bean.getNAME());
                            music.setArtist(bean.getARTIST());
                            musicList.add(music);
                        }
                        return musicList;
                    }
                }).compose(RxSchedulers.apply())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musicList) throws Exception {
                        dismissDialog();
                        getSearch().postValue(musicList);
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
                            getSongUrl().postValue(music);
                        }
                    }));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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
                    })
            );
        }
    }
}
