package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock

data class HomeBlock(
    val code: Int,
    val data: Data,
    val message: String
)

data class Data(
    val blockUUIDs: Any,
    val blocks: List<Block>,
    val cursor: Any,
    val guideToast: GuideToast,
    val hasMore: Boolean,
    val pageConfig: PageConfig
)

data class Block(
    val action: String,
    val actionType: String,
    val blockCode: String,
    val canClose: Boolean,
    val creatives: List<Creative>,
    val extInfo: Any,
    val showType: String,
    val uiElement: UiElementXX
)

data class GuideToast(
    val hasGuideToast: Boolean,
    val toastList: List<Any>
)

data class PageConfig(
    val abtest: List<String>,
    val fullscreen: Boolean,
    val homepageMode: String,
    val nodataToast: String,
    val refreshInterval: Int,
    val refreshToast: String,
    val showModeEntry: Boolean,
    val songLabelMarkLimit: Int,
    val songLabelMarkPriority: List<String>,
    val title: Any
)

data class Creative(
    val action: String,
    val actionType: String,
    val alg: String,
    val code: String,
    val creativeExtInfoVO: CreativeExtInfoVO,
    val creativeId: String,
    val creativeType: String,
    val logInfo: String,
    val position: Int,
    val resources: List<Resource>,
    val source: String,
    val uiElement: UiElementX
)

data class UiElementXX(
    val button: ButtonX,
    val mainTitle: MainTitleXX,
    val subTitle: SubTitleXX
)

data class CreativeExtInfoVO(
    val playCount: Long
)

data class Resource(
    val action: String,
    val actionType: String,
    val alg: String,
    val logInfo: Any,
    val resourceExtInfo: ResourceExtInfo,
    val resourceId: String,
    val resourceType: String,
    val resourceUrl: Any,
    val uiElement: UiElement,
    val valid: Boolean
)

data class UiElementX(
    val button: Button,
    val image: ImageX,
    val labelTexts: List<String>,
    val mainTitle: MainTitleX,
    val subTitle: SubTitleX
)

data class ResourceExtInfo(
    val alias: String,
    val artists: List<Artist>,
    val bigEvent: Boolean,
    val canSubscribe: Boolean,
    val commentSimpleData: CommentSimpleData,
    val endTime: Long,
    val eventId: String,
    val eventType: String,
    val highQuality: Boolean,
    val playCount: Long,
    val songData: SongData,
    val songPrivilege: SongPrivilege,
    val specialCover: Int,
    val startTime: Long,
    val subCount: Int,
    val subed: Boolean,
    val tags: List<String>
)

data class UiElement(
    val image: Image,
    val labelTexts: List<String>,
    val mainTitle: MainTitle,
    val subTitle: SubTitle
)

data class Artist(
    val albumSize: Int,
    val alias: List<Any>,
    val briefDesc: String,
    val id: Int,
    val img1v1Id: Int,
    val img1v1Url: String,
    val musicSize: Int,
    val name: String,
    val picId: Int,
    val picUrl: String,
    val topicPerson: Int,
    val trans: String
)

data class CommentSimpleData(
    val commentId: Long,
    val content: String,
    val threadId: Any,
    val userId: Long,
    val userName: Any
)

data class SongData(
    val album: Album,
    val alias: List<String>,
    val artists: List<ArtistXXX>,
    val audition: Any,
    val bMusic: BMusic,
    val commentThreadId: String,
    val copyFrom: String,
    val copyright: Int,
    val copyrightId: Long,
    val crbt: Any,
    val dayPlays: Int,
    val disc: String,
    val duration: Int,
    val fee: Int,
    val ftype: Int,
    val hMusic: HMusic,
    val hearTime: Int,
    val id: Long,
    val lMusic: LMusic,
    val mMusic: MMusic,
    val mark: Int,
    val mp3Url: Any,
    val mvid: Int,
    val name: String,
    val no: Int,
    val noCopyrightRcmd: Any,
    val originCoverType: Int,
    val originSongSimpleData: Any,
    val playedNum: Int,
    val popularity: Int,
    val position: Int,
    val ringtone: String,
    val rtUrl: Any,
    val rtUrls: List<Any>,
    val rtype: Int,
    val rurl: Any,
    val score: Int,
    val sign: Any,
    val single: Int,
    val starred: Boolean,
    val starredNum: Int,
    val status: Int,
    val transName: Any,
    val transNames: List<String>
)

data class SongPrivilege(
    val chargeInfoList: List<ChargeInfo>,
    val cp: Int,
    val cs: Boolean,
    val dl: Int,
    val downloadMaxbr: Int,
    val fee: Int,
    val fl: Int,
    val flag: Int,
    val id: Long,
    val maxbr: Int,
    val paidBigBang: Boolean,
    val payed: Int,
    val pc: Any,
    val pl: Int,
    val playMaxbr: Int,
    val preSell: Boolean,
    val realPayed: Int,
    val sp: Int,
    val st: Int,
    val subp: Int,
    val toast: Boolean
)

data class Album(
    val alias: List<Any>,
    val artist: ArtistX,
    val artists: List<ArtistXX>,
    val blurPicUrl: String,
    val briefDesc: String,
    val commentThreadId: String,
    val company: String,
    val companyId: Int,
    val copyrightId: Int,
    val description: String,
    val id: Long,
    val mark: Int,
    val name: String,
    val onSale: Boolean,
    val pic: Long,
    val picId: Long,
    val picId_str: String,
    val picUrl: String,
    val publishTime: Long,
    val size: Int,
    val songs: List<Any>,
    val status: Int,
    val subType: String,
    val tags: String,
    val transName: Any,
    val transNames: List<String>,
    val type: String
)

data class ArtistXXX(
    val albumSize: Int,
    val alias: List<Any>,
    val briefDesc: String,
    val id: Long,
    val img1v1Id: Int,
    val img1v1Url: String,
    val musicSize: Int,
    val name: String,
    val picId: Int,
    val picUrl: String,
    val topicPerson: Int,
    val trans: String
)

data class BMusic(
    val bitrate: Int,
    val dfsId: Int,
    val extension: String,
    val id: Long,
    val name: Any,
    val playTime: Int,
    val size: Int,
    val sr: Int,
    val volumeDelta: Float
)

data class HMusic(
    val bitrate: Int,
    val dfsId: Int,
    val extension: String,
    val id: Long,
    val name: Any,
    val playTime: Int,
    val size: Int,
    val sr: Int,
    val volumeDelta: Float
)

data class LMusic(
    val bitrate: Int,
    val dfsId: Int,
    val extension: String,
    val id: Long,
    val name: Any,
    val playTime: Int,
    val size: Int,
    val sr: Int,
    val volumeDelta: Float
)

data class MMusic(
    val bitrate: Int,
    val dfsId: Int,
    val extension: String,
    val id: Long,
    val name: Any,
    val playTime: Int,
    val size: Int,
    val sr: Int,
    val volumeDelta: Float
)

data class ArtistX(
    val albumSize: Int,
    val alias: List<Any>,
    val briefDesc: String,
    val id: Long,
    val img1v1Id: Int,
    val img1v1Url: String,
    val musicSize: Int,
    val name: String,
    val picId: Int,
    val picUrl: String,
    val topicPerson: Int,
    val trans: String
)

data class ArtistXX(
    val albumSize: Int,
    val alias: List<Any>,
    val briefDesc: String,
    val id: Long,
    val img1v1Id: Int,
    val img1v1Url: String,
    val musicSize: Int,
    val name: String,
    val picId: Int,
    val picUrl: String,
    val topicPerson: Int,
    val trans: String
)

data class ChargeInfo(
    val chargeMessage: Any,
    val chargeType: Int,
    val chargeUrl: Any,
    val rate: Int
)

data class Image(
    val imageUrl: String
)

data class MainTitle(
    val title: String
)

data class SubTitle(
    val title: String,
    val titleType: String
)

data class Button(
    val action: String,
    val actionType: String,
    val iconUrl: Any,
    val text: String
)

data class ImageX(
    val imageUrl: String
)

data class MainTitleX(
    val title: String
)

data class SubTitleX(
    val title: String
)

data class ButtonX(
    val action: String,
    val actionType: String,
    val iconUrl: Any,
    val text: String
)

data class MainTitleXX(
    val title: String
)

data class SubTitleXX(
    val title: String,
    val titleImgUrl: String
)