package com.fr1014.mycoludmusic.musicmanager.player

import android.os.Parcelable
import android.text.TextUtils
import com.fr1014.mycoludmusic.data.DataRepository
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongUrlEntity
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike
import com.fr1014.mycoludmusic.utils.CollectionUtils
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mycoludmusic.utils.sharedpreferences.SharedPreferencesConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import okhttp3.ResponseBody
import java.lang.NumberFormatException
import java.util.*

enum class MusicSource(val sourceType: Int) {
    WY_MUSIC(0),     // 网易
    KW_MUSIC(1)      // 酷我
}

/**
 * id : 歌曲id
 * artist : 歌手
 * title : 歌曲名
 * sourceType : 歌曲源（网易/酷我）
 * mvId : MV_Id
 */
@Parcelize
data class Music(var id: String, var artist: String, var title: String?, var mvId: String, var sourceType: Int) : Parcelable {
    var subTitle: String? = ""   // 歌曲别名
    var songUrl: String? = ""
    var imgUrl: String? = ""    // 歌曲图片地址
    var duration: Long = 0L   // 持续时间
    var original: String = ""  // 是否为原唱 1为原唱，0为非原唱
    var album: String? = ""   // 歌曲专辑
    var mvUrl: String = ""    // Mv地址
    val isOnlineMusic: Boolean = true //是否为在线歌曲)

//    constructor(id: String, sourceType: Int, artist: String) : this(id, sourceType) {
//        this.artist = artist
//    }

}

fun Music.changeMusicSource(id: String) {
    this.id = id
    this.sourceType = MusicSource.KW_MUSIC.sourceType
}

fun Music.isLikeMusic(musicLikes: MutableList<MusicLike>): Boolean {
    for (musicLike in musicLikes) {
        if (musicLike.id == id) {
            return true
        }
    }
    return false
}

/**
 * 判断两首歌曲是否为同一首歌曲
 */
fun Music.isSameMusic(music: Music?): Boolean {
    if (music == null) return false
    return music.id == id
}

/**
 * 判断歌曲是否在musicList中
 * @return 歌曲所在musicList中的下标，未找到时返回-1
 */
fun Music.indexOf(musicList: List<Music>): Int {
    if (CollectionUtils.isEmptyList(musicList)) return -1
    for (index in musicList.indices) {
        if (this.isSameMusic(musicList[index])) {
            return index
        }
    }
    return -1
}

/**
 * 获取酷我歌曲Id
 */
fun Music.getKWMusicId(): String = id.replace("MUSIC_", "")

/**
 * 随机播放的算法
 */
fun List<Music>.shuffle(): List<Music> {
    if (this.size == 1) return this
    var key: Int
    var temp: Music
    val rand = Random()
    val musics: MutableList<Music> = ArrayList<Music>(this)
    for (i in musics.indices) {
        key = rand.nextInt(musics.size - 1)
        temp = musics[i]
        musics[i] = musics[key]
        musics[key] = temp
    }
    return musics
}

fun Music.savePlayingMusic() {
    MusicListManageUtils.get().putMusic(SharedPreferencesConst.CURRENT_MUSIC, SharedPreferencesConst.CURRENT_MUSIC_KEY, this)
}

fun Music.loadRemoteCover(from: String) {
    MusicListManageUtils.get().loadMusicCover(this, from)
}

suspend fun Music.getSongUrl(dataRepository: DataRepository) {
    withContext(Dispatchers.IO) {
        try {
            when (sourceType) {
                0 -> {
                    val songUrlEntity: SongUrlEntity = dataRepository.kwyApiService.getWYSongUrl(id, System.currentTimeMillis().toString())
                    val songData = songUrlEntity.data[0]
                    val fee = songData.fee
                    //暂无音乐源或付费
                    if (TextUtils.isEmpty(songData.url) || fee == 1 || fee == 4) {
                        getWYFeeFromKW(dataRepository)
                    } else {
                        songUrl = songUrlEntity.data[0].url
                    }
                }
                1 -> {
                    val responseBody: ResponseBody = dataRepository.kkwApiService.getKWSongUrl(id)
                    songUrl = responseBody.string()
                }
                else -> {
                }
            }
        } catch (e: Exception) {
            //network error
            CommonUtils.toastShort("获取Url失败" + e.message)
        }
    }
}

//从酷我搜索网易的付费歌曲
suspend fun Music.getWYFeeFromKW(dataRepository: DataRepository) {
    withContext(Dispatchers.IO) {
        val kwSearchResult = dataRepository.kkwApiService.getKWSearchResult("$title $artist ", 0, 1)
        val searchList = kwSearchResult.abslist
        if (!CollectionUtils.isEmptyList(searchList)) {
            val musicRid = searchList[0].musicrid
            MusicListManageUtils.get().changeMusicId(this@getWYFeeFromKW, musicRid)
            id = musicRid
            sourceType = MusicSource.KW_MUSIC.sourceType
            getSongUrl(dataRepository)
        }
    }
}

suspend fun Music.getSongInfo(dataRepository: DataRepository) {
    withContext(Dispatchers.IO) {
        when (sourceType) {
            0 -> {
                val wySongDetail = dataRepository.kwyApiService.getWYSongDetail(id)
                if (!CollectionUtils.isEmptyList(wySongDetail.songs)) {
                    val songBean = wySongDetail.songs[0]
                    imgUrl = songBean.al.picUrl
                    duration = songBean.dt
                }
            }
            1 -> {
                try {
                    val kwSongDetail = dataRepository.kkwApiService.getKWSongDetail(getKWMusicId())
                    imgUrl = kwSongDetail.data.albumpic
                    duration = CommonUtils.stringToDuration(kwSongDetail.data.songTimeMinutes)
                } catch (e: NumberFormatException) {
                }
            }
            else -> {

            }
        }
    }
}
