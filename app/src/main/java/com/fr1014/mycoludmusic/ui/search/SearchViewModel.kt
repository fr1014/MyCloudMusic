package com.fr1014.mycoludmusic.ui.search

import android.app.Application
import android.text.TextUtils
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSearchEntity
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.rx.RxSchedulers
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.CommonUtil
import com.fr1014.mymvvm.base.BusLiveData
import com.google.gson.Gson
import io.reactivex.functions.Consumer
import java.util.*
import java.util.regex.Pattern

class SearchViewModel(application: Application, model: DataRepository) : CommonViewModel(application, model) {

    private val searchLive: BusLiveData<List<Music>>  = BusLiveData<List<Music>>()

    //搜索
    fun getSearchLive() = searchLive

    //获取搜索结果（网易）
    fun getSearchEntityWYY(keywords: String?, offset: Int) {
        addSubscribe(model.getWYSearch(keywords, offset)
                .map<List<Music>> { wySearchDetail ->
                    val musics: MutableList<Music> = ArrayList()
                    val songs = wySearchDetail.result.songs
                    for (song in songs) {
                        val music = Music()
                        val artists = song.ar
                        val sb = StringBuilder()
                        for (index in artists.indices) {
                            if (index < artists.size - 1) {
                                sb.append(artists[index].name).append("&")
                            } else {
                                sb.append(artists[index].name)
                            }
                        }
                        music.apply {
                            artist = sb.toString()
                            title = song.name
                            original = song.originCoverType.toString() + ""
                            id = song.id.toLong()
                        }
                        song.alia?.let {
                            music.subTitle = song.alia[0].toString()
                        }
                        musics.add(music)
                    }
                    musics
                }
                .compose(RxSchedulers.apply())
                .doOnSubscribe{
                    showDialog()
                }
                .subscribe {
                    musicList ->
                    dismissDialog()
                    searchLive.postValue(musicList)
                }
        )
    }

    //获取搜索结果（酷我）
    fun getSearchEntityKW(name: String?, count: Int) {
        addSubscribe(model.getSearchResult(name, count)
                .map<List<Music>> { responseBody ->
                    val result = responseBody.string()
                    val replace1 = result.replace("try{var jsondata=", "")
                    val replace2 = replace1.replace("; song(jsondata);}catch(e){jsonError(e)}", "")
                    val json = replace2.replace("'".toRegex(), "\"")
                    val gson = Gson()
                    val searchEntity = gson.fromJson(json, KWSearchEntity::class.java)
                    val musics: MutableList<Music> = ArrayList()
                    val abslistBeanList = searchEntity.abslist
                    for (abslistBean in abslistBeanList) {
                        val music = Music()
                        if (!TextUtils.isEmpty(abslistBean.artist)) {
//                                music.setArtist(abslistBean.getARTIST());
                            music.artist = abslistBean.artist.replace("&nbsp;".toRegex(), " ").replace("###".toRegex(), "&")
                        } else {
                            music.artist = abslistBean.aartist.replace("&nbsp;".toRegex(), " ").replace("###".toRegex(), "&")
                        }
                        music.title = abslistBean.songname.replace("&nbsp;".toRegex(), " ")
                        music.imgUrl = abslistBean.hts_MVPIC
                        music.musicrid = abslistBean.musicrid
                        musics.add(music)
                    }
                    musics
                }.compose(RxSchedulers.apply())
                .doOnSubscribe {
                    //showDialog();
                }
                .subscribe { musicList ->
                    //dismissDialog();
                    searchLive.postValue(musicList)
                })
    }

    //获取搜索结果（酷我）新的接口
    fun getSearchEntityKW(name: String, page: Int, count: Int) {
        addSubscribe(model.getKWSearchResult(name, page, count)
                .map<List<Music>> { kwNewSearchEntity ->
                    val list = kwNewSearchEntity.abslist
                    val musicList: MutableList<Music> = ArrayList()
                    val pattern = "([\\s\\S]+)-([\\s\\S]+)"
                    val r = Pattern.compile(pattern)
                    for (bean in list) {
                        val m = r.matcher(bean.name)
                        val music = Music()
                        music.apply {
                            musicrid = bean.musicrid
                            artist = bean.artist
                            original = bean.originalsongtype
                            album = bean.album
                            subTitle = music.subTitle
                            if (m.matches()) {
                                title = m.group(1)
                                subTitle = m.group(2)
                            } else {
                                title = bean.name
                            }
                        }
                        musicList.add(music)
                    }
                    musicList
                }.compose(RxSchedulers.apply())
                .doOnSubscribe { showDialog() }
                .subscribe { musicList ->
                    dismissDialog()
                    searchLive.postValue(musicList)
                })
    }
}