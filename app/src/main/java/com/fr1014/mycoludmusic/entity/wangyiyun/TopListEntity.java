package com.fr1014.mycoludmusic.entity.wangyiyun;

import java.util.List;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 *
 * 所有榜单
 */
public class TopListEntity {

    /**
     * code : 200
     * list : [{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,
     * "updateFrequency":"每天更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,
     * "titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,
     * "trackNumberUpdateTime":1598920083255,"adType":0,"subscribedCount":3318581,"cloudTrackCount":0,
     * "userId":1,"createTime":1404115136883,"highQuality":false,"updateTime":1598920083789,
     * "coverImgId":18696095720518496,"newImported":false,"anonimous":false,"specialType":10,
     * "totalDuration":0,"coverImgUrl":"http://p1.music.126.net/DrRIg6CrgDfVLEph9SNh7w==/18696095720518497.jpg",
     * "trackCount":100,"commentThreadId":"A_PL_0_19723756","privacy":0,"trackUpdateTime":1598930069422,
     * "playCount":3493488640,"ordered":true,"tags":[],
     * "description":"云音乐中每天热度上升最快的100首单曲，每日更新。","status":0,"name":"云音乐飙升榜",
     * "id":19723756,"coverImgId_str":"18696095720518497","ToplistType":"S"},{"subscribers":[],"subscribed":null,
     * "creator":null,"artists":null,"tracks":null,"updateFrequency":"每天更新","backgroundCoverId":0,
     * "backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,
     * "recommendInfo":null,"trackNumberUpdateTime":1598920203968,"adType":0,"subscribedCount":2345717,
     * "cloudTrackCount":0,"userId":1,"createTime":1378721398225,"highQuality":false,"updateTime":1598920264861,
     * "coverImgId":18713687906568050,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,
     * "coverImgUrl":"http://p1.music.126.net/N2HO5xfYEqyQ8q6oxCw8IQ==/18713687906568048.jpg","trackCount":100,
     * "commentThreadId":"A_PL_0_3779629","privacy":0,"trackUpdateTime":1598981535880,"playCount":1920751616,
     * "ordered":true,"tags":[],"description":"云音乐新歌榜：云音乐用户一周内收听所有新歌（一月内最新发行） 官方TOP排行榜，每天更新。",
     * "status":0,"name":"云音乐新歌榜","id":3779629,"coverImgId_str":"18713687906568048","ToplistType":"N"},{"subscribers":[],
     * "subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周四更新","backgroundCoverId":0,
     * "backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,
     * "recommendInfo":null,"trackNumberUpdateTime":1598587055254,"adType":0,"subscribedCount":634180,
     * "cloudTrackCount":0,"userId":201586,"createTime":1374732325894,"highQuality":false,
     * "updateTime":1598587055310,"coverImgId":18740076185638788,"newImported":false,
     * "anonimous":false,"specialType":10,"totalDuration":0,
     * "coverImgUrl":"http://p1.music.126.net/sBzD11nforcuh1jdLSgX7g==/18740076185638788.jpg",
     * "trackCount":100,"commentThreadId":"A_PL_0_2884035","privacy":0,"trackUpdateTime":1598879891177,
     * "playCount":439484384,"ordered":true,"tags":[],"description":"云音乐独立原创音乐人作品官方榜单，以推荐优秀原创作品为目的。每周四网易云音乐首发。申请网易音乐人：http://music.163.com/nmusician/","status":0,
     * "name":"网易原创歌曲榜","id":2884035,"coverImgId_str":"18740076185638788","ToplistType":"O"},{"subscribers":[],
     * "subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周四更新",
     * "backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,
     * "opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598487501835,"adType":0,
     * "subscribedCount":9946814,"cloudTrackCount":0,"userId":1,"createTime":1378721406014,
     * "highQuality":false,"updateTime":1598487502104,"coverImgId":18708190348409092,
     * "newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/GhhuF6Ep5Tq9IEvLsyCN7w==/18708190348409091.jpg",
     * "trackCount":200,"commentThreadId":"A_PL_0_3778678","privacy":0,"trackUpdateTime":1598841963730,"playCount":7347636224,
     * "ordered":true,"tags":[],"description":"云音乐热歌榜：云音乐用户一周内收听所有线上歌曲 官方TOP排行榜，每周四更新。",
     * "status":0,"name":"云音乐热歌榜","id":3778678,"coverImgId_str":"18708190348409091","ToplistType":"H"},
     * {"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新",
     * "backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,
     * "englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598580673756,
     * "adType":0,"subscribedCount":503466,"cloudTrackCount":0,"userId":1,"createTime":1510290389440,
     * "highQuality":false,"updateTime":1598580673862,"coverImgId":109951164353220910,"newImported":false,
     * "anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/y8zyTt4HwXbJqB31aQKz1A==/109951164353220919.jpg",
     * "trackCount":50,"commentThreadId":"A_PL_0_991319590","privacy":0,"trackUpdateTime":1598611460024,"playCount":217365440,"ordered":true,"tags":["华语","说唱"],"description":"云音乐原创说唱音乐人作品官方榜单，每周五更新。以云音乐用户一周播放热度为主，收录3个月内发行的原创说唱作品，按照综合数据排名取前50名。申请网易音乐人：http://music.163.com/nmusician","status":0,"name":"云音乐说唱榜","id":991319590,"coverImgId_str":"109951164353220919"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周四更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598494336400,"adType":0,"subscribedCount":361776,"cloudTrackCount":0,"userId":1,"createTime":1430968920537,"highQuality":false,"updateTime":1598494336583,"coverImgId":18681802069355170,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/BzSxoj6O1LQPlFceDn-LKw==/18681802069355169.jpg","trackCount":100,"commentThreadId":"A_PL_0_71384707","privacy":0,"trackUpdateTime":1598838735731,"playCount":46901560,"ordered":true,"tags":["古典"],"description":"云音乐用户一周内收听所有古典音乐官方TOP排行榜，每周四更新。","status":0,"name":"云音乐古典音乐榜","id":71384707,"coverImgId_str":"18681802069355169"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598589611768,"adType":0,"subscribedCount":1127666,"cloudTrackCount":0,"userId":1,"createTime":1510825632233,"highQuality":false,"updateTime":1598589611826,"coverImgId":18822539557941308,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/5tgOCD4jiPKBGt7znJl-2g==/18822539557941307.jpg","trackCount":50,"commentThreadId":"A_PL_0_1978921795","privacy":0,"trackUpdateTime":1598839921439,"playCount":221773328,"ordered":true,"tags":["电子"],"description":"云音乐用户一周内收听电子音乐官方TOP排行榜，每周五更新","status":0,"name":"云音乐电音榜","id":1978921795,"coverImgId_str":"18822539557941307"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598010044887,"adType":0,"subscribedCount":2610736,"cloudTrackCount":0,"userId":1473357007,"createTime":1527831892491,"highQuality":false,"updateTime":1598010044952,"coverImgId":109951164174523460,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/oUxnXXvM33OUHxxukYnUjQ==/109951164174523461.jpg","trackCount":100,"commentThreadId":"A_PL_0_2250011882","privacy":0,"trackUpdateTime":1598837626402,"playCount":672981888,"ordered":true,"tags":["榜单"],"description":"抖音排行榜，每周五更新。","status":0,"name":"抖音排行榜","id":2250011882,"coverImgId_str":"109951164174523461"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周一更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1561995353888,"adType":0,"subscribedCount":83033,"cloudTrackCount":0,"userId":106733386,"createTime":1547092729345,"highQuality":false,"updateTime":1561995354595,"coverImgId":109951163785427940,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/XbjRDARP1xv5a-40ZDOy6A==/109951163785427934.jpg","trackCount":39,"commentThreadId":"A_PL_0_2617766278","privacy":0,"trackUpdateTime":1598887025411,"playCount":46272144,"ordered":true,"tags":["华语","流行"],"description":"LOOK直播 - 「LOOK新声代2」活动官方榜单，旨在推介超人气单曲和小众优质原创～","status":0,"name":"新声榜","id":2617766278,"coverImgId_str":"109951163785427934"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周四更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598494412054,"adType":0,"subscribedCount":200599,"cloudTrackCount":0,"userId":1,"createTime":1430968935040,"highQuality":false,"updateTime":1598494412202,"coverImgId":18752170813539664,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/vttjtRjL75Q4DEnjRsO8-A==/18752170813539664.jpg","trackCount":100,"commentThreadId":"A_PL_0_71385702","privacy":0,"trackUpdateTime":1598602730577,"playCount":55278108,"ordered":true,"tags":[],"description":"云音乐用户一周内收听所有ACG音乐官方TOP排行榜，每周四更新。","status":0,"name":"云音乐ACG音乐榜","id":71385702,"coverImgId_str":"18752170813539664"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周四更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598500802404,"adType":0,"subscribedCount":168545,"cloudTrackCount":0,"userId":1,"createTime":1496201691281,"highQuality":false,"updateTime":1598500802527,"coverImgId":18737877162497500,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/vs-cMh49e6qPEorHuhU07A==/18737877162497499.jpg","trackCount":100,"commentThreadId":"A_PL_0_745956260","privacy":0,"trackUpdateTime":1598839685116,"playCount":56015360,"ordered":true,"tags":["韩语","榜单"],"description":"云音乐用户一周内收听所有韩语歌曲官方TOP排行榜，每周四更新。","status":0,"name":"云音乐韩语榜","id":745956260,"coverImgId_str":"18737877162497499"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598609264335,"adType":0,"subscribedCount":219061,"cloudTrackCount":0,"userId":48102,"createTime":1395988377813,"highQuality":false,"updateTime":1598609264421,"coverImgId":109951163424197390,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/8-GBrukQ3BHhs4CmK6qE4w==/109951163424197392.jpg","trackCount":19,"commentThreadId":"A_PL_0_10520166","privacy":0,"trackUpdateTime":1598860641824,"playCount":68906048,"ordered":true,"tags":["电子","榜单"],"description":"▲▲▲本榜排名按作品发行时间顺序▲▲▲网易云音乐联合网易放刺、Loopy、加菲众、DJ WENGWENG（灯笼Club）、3ASiC（同步计划）、DJ Senders（沉睡电台）、Eiasn电音厂牌、电悦台（EDM Station） \n打造云音乐\u201c国电榜\u201d ! 每周五为大家带来网易电子音乐人优质新作！","status":0,"name":"云音乐国电榜","id":10520166,"coverImgId_str":"109951163424197392"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周三更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1596607839953,"adType":0,"subscribedCount":33378,"cloudTrackCount":0,"userId":270539485,"createTime":1513838619821,"highQuality":false,"updateTime":1596607843451,"coverImgId":109951163089272200,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/0_6_Efe9m0D0NtghOxinUg==/109951163089272193.jpg","trackCount":20,"commentThreadId":"A_PL_0_2023401535","privacy":0,"trackUpdateTime":1597829072560,"playCount":8178438,"ordered":true,"tags":["欧美","流行","英伦"],"description":"英国权威音乐杂志《Q》中文版&网易云音乐联合呈现榜单TOP20，英国《Q》杂志权威推荐。每周三同步更新。","status":0,"name":"英国Q杂志中文版周榜","id":2023401535,"coverImgId_str":"109951163089272193"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598587201185,"adType":0,"subscribedCount":92262,"cloudTrackCount":0,"userId":6790397,"createTime":1512703064327,"highQuality":false,"updateTime":1598587201289,"coverImgId":109951163078922990,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/CUqQp33MZf_m0BwH4u0V6A==/109951163078922993.jpg","trackCount":50,"commentThreadId":"A_PL_0_2006508653","privacy":0,"trackUpdateTime":1598837434259,"playCount":22189320,"ordered":true,"tags":["游戏"],"description":"无音乐，不游戏。人气电竞主播联手推荐，最热最潮电竞歌曲榜单，电竞迷们的必备收藏！","status":0,"name":"电竞音乐榜","id":2006508653,"coverImgId_str":"109951163078922993"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周一更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598845661587,"adType":0,"subscribedCount":270578,"cloudTrackCount":0,"userId":48333,"createTime":1361239766844,"highQuality":false,"updateTime":1598845661754,"coverImgId":18930291695438268,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/VQOMRRix9_omZbg4t-pVpw==/18930291695438269.jpg","trackCount":98,"commentThreadId":"A_PL_0_180106","privacy":0,"trackUpdateTime":1598868772905,"playCount":113605480,"ordered":true,"tags":["榜单","欧美"],"description":"UK排行榜","status":0,"name":"UK排行榜周榜","id":180106,"coverImgId_str":"18930291695438269"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周三更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598410253242,"adType":0,"subscribedCount":1185012,"cloudTrackCount":0,"userId":48171,"createTime":1358823076818,"highQuality":false,"updateTime":1598410257808,"coverImgId":18641120139148116,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/EBRqPmY8k8qyVHyF8AyjdQ==/18641120139148117.jpg","trackCount":100,"commentThreadId":"A_PL_0_60198","privacy":0,"trackUpdateTime":1598493243067,"playCount":442593376,"ordered":true,"tags":["流行","欧美","榜单"],"description":"美国Billboard排行榜","status":0,"name":"美国Billboard周榜","id":60198,"coverImgId_str":"18641120139148117"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周三更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598434650954,"adType":0,"subscribedCount":257798,"cloudTrackCount":0,"userId":1589393,"createTime":1378886589466,"highQuality":false,"updateTime":1598434651182,"coverImgId":18613632348448740,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/A61n94BjWAb-ql4xpwpYcg==/18613632348448741.jpg","trackCount":77,"commentThreadId":"A_PL_0_3812895","privacy":0,"trackUpdateTime":1598855808799,"playCount":82778712,"ordered":true,"tags":["欧美","电子","榜单"],"description":"Beatport全球电子舞曲排行榜TOP100（本榜每周三更新）","status":0,"name":"Beatport全球电子舞曲榜","id":3812895,"coverImgId_str":"18613632348448741"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1596793320079,"adType":0,"subscribedCount":221066,"cloudTrackCount":0,"userId":30728956,"createTime":1405653093230,"highQuality":false,"updateTime":1598591072675,"coverImgId":19174383276805160,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/H4Y7jxd_zwygcAmPMfwJnQ==/19174383276805159.jpg","trackCount":20,"commentThreadId":"A_PL_0_21845217","privacy":0,"trackUpdateTime":1598848467868,"playCount":54434228,"ordered":true,"tags":["华语","KTV","榜单"],"description":"KTV唛榜是目前国内首个以全国超过200家KTV点歌平台真实数据的当红歌曲榜单。所涉及的KTV店铺覆盖全国近100多个城市，囊括一、二、三线各级城市及地区。在综合全国各地KTV点唱数据的前提下进行汇总与统计。为了保证信息的及时性，唛榜每周五更新。提供给K迷们最新和最准确的数据。","status":0,"name":"KTV唛榜","id":21845217,"coverImgId_str":"19174383276805159"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周一更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598846171221,"adType":0,"subscribedCount":312363,"cloudTrackCount":0,"userId":48308,"createTime":1398047444743,"highQuality":false,"updateTime":1598846171361,"coverImgId":109951163601178880,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/WTpbsVfxeB6qDs_3_rnQtg==/109951163601178881.jpg","trackCount":95,"commentThreadId":"A_PL_0_11641012","privacy":0,"trackUpdateTime":1598981017375,"playCount":101372024,"ordered":true,"tags":["榜单"],"description":"iTunes榜Top100","status":0,"name":"iTunes榜","id":11641012,"coverImgId_str":"109951163601178881"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周三更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598431828009,"adType":0,"subscribedCount":133097,"cloudTrackCount":0,"userId":48160,"createTime":1357635084874,"highQuality":false,"updateTime":1598431828071,"coverImgId":19029247741938160,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/Rgqbqsf4b3gNOzZKxOMxuw==/19029247741938160.jpg","trackCount":20,"commentThreadId":"A_PL_0_60131","privacy":0,"trackUpdateTime":1598744172724,"playCount":46879436,"ordered":true,"tags":["榜单","日语"],"description":"日本Oricon数字单曲周榜，每周三更新，欢迎关注。","status":0,"name":"日本Oricon数字单曲周榜","id":60131,"coverImgId_str":"19029247741938160"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周一更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598259673742,"adType":0,"subscribedCount":102545,"cloudTrackCount":0,"userId":48337,"createTime":1359703138872,"highQuality":false,"updateTime":1598259673836,"coverImgId":19187577416338508,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/54vZEZ-fCudWZm6GH7I55w==/19187577416338508.jpg","trackCount":20,"commentThreadId":"A_PL_0_120001","privacy":0,"trackUpdateTime":1598669163738,"playCount":28258912,"ordered":true,"tags":[],"description":"Hit FM Top 20 Countdown 第36期","status":0,"name":"Hit FM Top榜","id":120001,"coverImgId_str":"19187577416338508"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周一更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598259000076,"adType":0,"subscribedCount":35841,"cloudTrackCount":0,"userId":48174,"createTime":1359690215675,"highQuality":false,"updateTime":1598259000135,"coverImgId":18646617697286900,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/wqi4TF4ILiTUUL5T7zhwsQ==/18646617697286899.jpg","trackCount":8,"commentThreadId":"A_PL_0_112463","privacy":0,"trackUpdateTime":1598259000141,"playCount":13691400,"ordered":true,"tags":["流行"],"description":"資料來源統計： \u203b唱片行銷售量佔20%(含玫瑰大眾、g-music、五大、佳佳、博客來等各大唱片行) \u203b數位音樂下載佔30%(含 iTunes、KK box、myMusic、Omusic、各鈴聲下載榜) \u203bHit Fm聯播網AIR PLAY電台播出率佔50%","status":0,"name":"台湾Hito排行榜","id":112463,"coverImgId_str":"18646617697286899"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周四更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598496017859,"adType":0,"subscribedCount":308599,"cloudTrackCount":0,"userId":1,"createTime":1558493373769,"highQuality":false,"updateTime":1598496018174,"coverImgId":109951164091703580,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/c0iThrYPpnFVgFvU6JCVXQ==/109951164091703579.jpg","trackCount":200,"commentThreadId":"A_PL_0_2809513713","privacy":0,"trackUpdateTime":1598881100360,"playCount":47450108,"ordered":true,"tags":[],"description":"云音乐用户一周内收听所有欧美歌曲官方TOP排行榜，每周四更新。\nWestern Hit Chart (updated every Thursday)","status":0,"name":"云音乐欧美热歌榜","id":2809513713,"coverImgId_str":"109951164091703579"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每天更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598923508300,"adType":0,"subscribedCount":155376,"cloudTrackCount":0,"userId":1,"createTime":1558493214795,"highQuality":false,"updateTime":1598923508418,"coverImgId":109951164091690480,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/Zb8AL5xdl9-_7WIyAhRLbw==/109951164091690485.jpg","trackCount":100,"commentThreadId":"A_PL_0_2809577409","privacy":0,"trackUpdateTime":1598982115801,"playCount":56175892,"ordered":true,"tags":[],"description":"云音乐用户一周内收听所有欧美新歌（一月内最新发行）官方TOP排行榜，每天更新。\nWestern New Release Chart (new songs released in last 30 days, updated daily)\n","status":0,"name":"云音乐欧美新歌榜","id":2809577409,"coverImgId_str":"109951164091690485"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598593874067,"adType":0,"subscribedCount":54452,"cloudTrackCount":0,"userId":5190793,"createTime":1409825013948,"highQuality":false,"updateTime":1598593874113,"coverImgId":109951162873641550,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/6O0ZEnO-I_RADBylVypprg==/109951162873641556.jpg","trackCount":20,"commentThreadId":"A_PL_0_27135204","privacy":0,"trackUpdateTime":1598594299967,"playCount":18873228,"ordered":true,"tags":["榜单"],"description":"法国NRJ电台（national Radio de Jeunes）成立于1981年，总部位于法国巴黎。是法国最受欢迎的音乐电台和听众最多的广播电台之一。NRJ音乐奖素有法国的\u201c格莱美\u201d之称。此榜单针对NRJ电台法国本土热门歌曲排行。【每周五更新】","status":0,"name":"法国 NRJ Vos Hits 周榜","id":27135204,"coverImgId_str":"109951162873641556"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每天更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598932810253,"adType":0,"subscribedCount":23326,"cloudTrackCount":0,"userId":1,"createTime":1569549838610,"highQuality":false,"updateTime":1598932810410,"coverImgId":109951164432300400,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/SkGlKQ6acixthb77VlD9eQ==/109951164432300406.jpg","trackCount":100,"commentThreadId":"A_PL_0_3001835560","privacy":0,"trackUpdateTime":1598932810434,"playCount":1386202,"ordered":true,"tags":[],"description":"云音乐中每天热度上升最快的100首ACG动画单曲，每日更新。","status":0,"name":"云音乐ACG动画榜","id":3001835560,"coverImgId_str":"109951164432300406"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每天更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598932820412,"adType":0,"subscribedCount":4760,"cloudTrackCount":0,"userId":1,"createTime":1569549896656,"highQuality":false,"updateTime":1598932820569,"coverImgId":109951164432303700,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/hivOOHMwEmnn9s_6rgZwEQ==/109951164432303700.jpg","trackCount":100,"commentThreadId":"A_PL_0_3001795926","privacy":0,"trackUpdateTime":1598932820584,"playCount":401422,"ordered":true,"tags":[],"description":"云音乐中每天热度上升最快的100首ACG游戏单曲，每日更新。","status":0,"name":"云音乐ACG游戏榜","id":3001795926,"coverImgId_str":"109951164432303700"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每天更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598932830433,"adType":0,"subscribedCount":4734,"cloudTrackCount":0,"userId":1,"createTime":1569549925472,"highQuality":false,"updateTime":1598932830587,"coverImgId":109951164432303700,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/Ag7RyRCYiINcd9EtRXf6xA==/109951164432303690.jpg","trackCount":100,"commentThreadId":"A_PL_0_3001890046","privacy":0,"trackUpdateTime":1598932830600,"playCount":217243,"ordered":true,"tags":[],"description":null,"status":0,"name":"云音乐ACG VOCALOID榜","id":3001890046,"coverImgId_str":"109951164432303690"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周四更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1596096675051,"adType":0,"subscribedCount":23555,"cloudTrackCount":0,"userId":1861504131,"createTime":1575963485139,"highQuality":false,"updateTime":1598539403391,"coverImgId":109951164540938460,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/E5xPfNqD1rp6dB1VPOlLUQ==/109951164540938467.jpg","trackCount":49,"commentThreadId":"A_PL_0_3112516681","privacy":0,"trackUpdateTime":1598539403389,"playCount":3734054,"ordered":true,"tags":[],"description":"中国新乡村音乐排行榜是国内首个以\u201c新乡村\u201d为主题的音乐榜单。该主题歌曲能够反映出\u201c乡音、乡情、乡恋、乡愁\u201d的情感，让听众感受到\u201c新时代、新乡村、新音乐\u201d。榜单选取符合条件且最近14天热度最高的前50歌曲，每周四更新。","status":0,"name":"中国新乡村音乐排行榜","id":3112516681,"coverImgId_str":"109951164540938467"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周二更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598929379787,"adType":0,"subscribedCount":21368,"cloudTrackCount":0,"userId":1,"createTime":1591863000459,"highQuality":false,"updateTime":1598929379909,"coverImgId":109951165165699700,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/gA3BalzlKrHZlHJqcK0iWw==/109951165165699702.jpg","trackCount":100,"commentThreadId":"A_PL_0_5059644681","privacy":0,"trackUpdateTime":1598933093284,"playCount":3110789,"ordered":true,"tags":[],"description":"云音乐用户一周内收听所有日语歌曲官方TOP排行榜，每周二更新。","status":0,"name":"云音乐日语榜","id":5059644681,"coverImgId_str":"109951165165699702"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598589720339,"adType":0,"subscribedCount":130077,"cloudTrackCount":0,"userId":1,"createTime":1591863052757,"highQuality":false,"updateTime":1598589720507,"coverImgId":109951165165693950,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/J6x0W62ONLVsU93rXUhVXw==/109951165165693959.jpg","trackCount":50,"commentThreadId":"A_PL_0_5059661515","privacy":0,"trackUpdateTime":1598836973197,"playCount":19074956,"ordered":true,"tags":[],"description":"云音乐用户一周内收听所有民谣歌曲官方TOP排行榜，每周五更新。","status":0,"name":"云音乐民谣榜","id":5059661515,"coverImgId_str":"109951165165693959"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598589666864,"adType":0,"subscribedCount":11901,"cloudTrackCount":0,"userId":1,"createTime":1591863213389,"highQuality":false,"updateTime":1598589667077,"coverImgId":109951165165700160,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/HnioDkCNmchZl5FKKeqvNQ==/109951165165700167.jpg","trackCount":50,"commentThreadId":"A_PL_0_5059633707","privacy":0,"trackUpdateTime":1598838545596,"playCount":2632858,"ordered":true,"tags":[],"description":"云音乐用户一周内收听所有摇滚歌曲官方TOP排行榜，每周五更新。","status":0,"name":"云音乐摇滚榜","id":5059633707,"coverImgId_str":"109951165165700167"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周五更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598589755631,"adType":0,"subscribedCount":88706,"cloudTrackCount":0,"userId":1,"createTime":1591863258438,"highQuality":false,"updateTime":1598589755705,"coverImgId":109951165165696820,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/0BcoQQ36qj56JnGKzY8QbQ==/109951165165696810.jpg","trackCount":50,"commentThreadId":"A_PL_0_5059642708","privacy":0,"trackUpdateTime":1598836551601,"playCount":12231167,"ordered":true,"tags":[],"description":"云音乐用户一周内收听所有古风歌曲官方TOP排行榜，每周五更新。","status":0,"name":"云音乐古风榜","id":5059642708,"coverImgId_str":"109951165165696810"},{"subscribers":[],"subscribed":null,"creator":null,"artists":null,"tracks":null,"updateFrequency":"每周一更新","backgroundCoverId":0,"backgroundCoverUrl":null,"titleImage":0,"titleImageUrl":null,"englishTitle":null,"opRecommend":false,"recommendInfo":null,"trackNumberUpdateTime":1598952430499,"adType":0,"subscribedCount":897,"cloudTrackCount":0,"userId":3328420976,"createTime":1598343964280,"highQuality":false,"updateTime":1598952430499,"coverImgId":109951165268458340,"newImported":false,"anonimous":false,"specialType":10,"totalDuration":0,"coverImgUrl":"http://p1.music.126.net/fLCv2juI92WPzPwqpklr-Q==/109951165268458344.jpg","trackCount":100,"commentThreadId":"A_PL_0_5201625538","privacy":0,"trackUpdateTime":1598952581188,"playCount":220486,"ordered":true,"tags":["流行"],"description":"云音乐用户一周内所有云贝推歌的歌曲官方精选TOP排行，每周一更新","status":0,"name":"云贝推歌榜","id":5201625538,"coverImgId_str":"109951165268458344"}]
     * artistToplist : {"coverUrl":"http://p1.music.126.net/MJdmNzZwTCz0b4IpHJV6Wg==/109951162865487429.jpg","name":"云音乐歌手榜","upateFrequency":"每天更新","position":5,"updateFrequency":"每天更新"}
     */

    private int code;
    private ArtistToplistBean artistToplist;
    private List<ListBean> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArtistToplistBean getArtistToplist() {
        return artistToplist;
    }

    public void setArtistToplist(ArtistToplistBean artistToplist) {
        this.artistToplist = artistToplist;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ArtistToplistBean {
        /**
         * coverUrl : http://p1.music.126.net/MJdmNzZwTCz0b4IpHJV6Wg==/109951162865487429.jpg
         * name : 云音乐歌手榜
         * upateFrequency : 每天更新
         * position : 5
         * updateFrequency : 每天更新
         */

        private String coverUrl;
        private String name;
        private String upateFrequency;
        private int position;
        private String updateFrequency;

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUpateFrequency() {
            return upateFrequency;
        }

        public void setUpateFrequency(String upateFrequency) {
            this.upateFrequency = upateFrequency;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getUpdateFrequency() {
            return updateFrequency;
        }

        public void setUpdateFrequency(String updateFrequency) {
            this.updateFrequency = updateFrequency;
        }
    }

    public static class ListBean {
        /**
         * subscribers : []
         * subscribed : null
         * creator : null
         * artists : null
         * tracks : null
         * updateFrequency : 每天更新
         * backgroundCoverId : 0
         * backgroundCoverUrl : null
         * titleImage : 0
         * titleImageUrl : null
         * englishTitle : null
         * opRecommend : false
         * recommendInfo : null
         * trackNumberUpdateTime : 1598920083255
         * adType : 0
         * subscribedCount : 3318581
         * cloudTrackCount : 0
         * userId : 1
         * createTime : 1404115136883
         * highQuality : false
         * updateTime : 1598920083789
         * coverImgId : 18696095720518496
         * newImported : false
         * anonimous : false
         * specialType : 10
         * totalDuration : 0
         * coverImgUrl : http://p1.music.126.net/DrRIg6CrgDfVLEph9SNh7w==/18696095720518497.jpg
         * trackCount : 100
         * commentThreadId : A_PL_0_19723756
         * privacy : 0
         * trackUpdateTime : 1598930069422
         * playCount : 3493488640
         * ordered : true
         * tags : []
         * description : 云音乐中每天热度上升最快的100首单曲，每日更新。
         * status : 0
         * name : 云音乐飙升榜
         * id : 19723756
         * coverImgId_str : 18696095720518497
         * ToplistType : S
         */

        private Object subscribed;
        private Object creator;
        private Object artists;
        private Object tracks;
        private String updateFrequency;
        private int backgroundCoverId;
        private Object backgroundCoverUrl;
        private int titleImage;
        private Object titleImageUrl;
        private Object englishTitle;
        private boolean opRecommend;
        private Object recommendInfo;
        private long trackNumberUpdateTime;
        private int adType;
        private int subscribedCount;
        private int cloudTrackCount;
        private long userId;
        private long createTime;
        private boolean highQuality;
        private long updateTime;
        private long coverImgId;
        private boolean newImported;
        private boolean anonimous;
        private int specialType;
        private int totalDuration;
        private String coverImgUrl;
        private int trackCount;
        private String commentThreadId;
        private int privacy;
        private long trackUpdateTime;
        private long playCount;
        private boolean ordered;
        private String description;
        private int status;
        private String name;
        private long id;
        private String coverImgId_str;
        private String ToplistType;
        private List<?> subscribers;
        private List<?> tags;

        public Object getSubscribed() {
            return subscribed;
        }

        public void setSubscribed(Object subscribed) {
            this.subscribed = subscribed;
        }

        public Object getCreator() {
            return creator;
        }

        public void setCreator(Object creator) {
            this.creator = creator;
        }

        public Object getArtists() {
            return artists;
        }

        public void setArtists(Object artists) {
            this.artists = artists;
        }

        public Object getTracks() {
            return tracks;
        }

        public void setTracks(Object tracks) {
            this.tracks = tracks;
        }

        public String getUpdateFrequency() {
            return updateFrequency;
        }

        public void setUpdateFrequency(String updateFrequency) {
            this.updateFrequency = updateFrequency;
        }

        public int getBackgroundCoverId() {
            return backgroundCoverId;
        }

        public void setBackgroundCoverId(int backgroundCoverId) {
            this.backgroundCoverId = backgroundCoverId;
        }

        public Object getBackgroundCoverUrl() {
            return backgroundCoverUrl;
        }

        public void setBackgroundCoverUrl(Object backgroundCoverUrl) {
            this.backgroundCoverUrl = backgroundCoverUrl;
        }

        public int getTitleImage() {
            return titleImage;
        }

        public void setTitleImage(int titleImage) {
            this.titleImage = titleImage;
        }

        public Object getTitleImageUrl() {
            return titleImageUrl;
        }

        public void setTitleImageUrl(Object titleImageUrl) {
            this.titleImageUrl = titleImageUrl;
        }

        public Object getEnglishTitle() {
            return englishTitle;
        }

        public void setEnglishTitle(Object englishTitle) {
            this.englishTitle = englishTitle;
        }

        public boolean isOpRecommend() {
            return opRecommend;
        }

        public void setOpRecommend(boolean opRecommend) {
            this.opRecommend = opRecommend;
        }

        public Object getRecommendInfo() {
            return recommendInfo;
        }

        public void setRecommendInfo(Object recommendInfo) {
            this.recommendInfo = recommendInfo;
        }

        public long getTrackNumberUpdateTime() {
            return trackNumberUpdateTime;
        }

        public void setTrackNumberUpdateTime(long trackNumberUpdateTime) {
            this.trackNumberUpdateTime = trackNumberUpdateTime;
        }

        public int getAdType() {
            return adType;
        }

        public void setAdType(int adType) {
            this.adType = adType;
        }

        public int getSubscribedCount() {
            return subscribedCount;
        }

        public void setSubscribedCount(int subscribedCount) {
            this.subscribedCount = subscribedCount;
        }

        public int getCloudTrackCount() {
            return cloudTrackCount;
        }

        public void setCloudTrackCount(int cloudTrackCount) {
            this.cloudTrackCount = cloudTrackCount;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public boolean isHighQuality() {
            return highQuality;
        }

        public void setHighQuality(boolean highQuality) {
            this.highQuality = highQuality;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getCoverImgId() {
            return coverImgId;
        }

        public void setCoverImgId(long coverImgId) {
            this.coverImgId = coverImgId;
        }

        public boolean isNewImported() {
            return newImported;
        }

        public void setNewImported(boolean newImported) {
            this.newImported = newImported;
        }

        public boolean isAnonimous() {
            return anonimous;
        }

        public void setAnonimous(boolean anonimous) {
            this.anonimous = anonimous;
        }

        public int getSpecialType() {
            return specialType;
        }

        public void setSpecialType(int specialType) {
            this.specialType = specialType;
        }

        public int getTotalDuration() {
            return totalDuration;
        }

        public void setTotalDuration(int totalDuration) {
            this.totalDuration = totalDuration;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public int getTrackCount() {
            return trackCount;
        }

        public void setTrackCount(int trackCount) {
            this.trackCount = trackCount;
        }

        public String getCommentThreadId() {
            return commentThreadId;
        }

        public void setCommentThreadId(String commentThreadId) {
            this.commentThreadId = commentThreadId;
        }

        public int getPrivacy() {
            return privacy;
        }

        public void setPrivacy(int privacy) {
            this.privacy = privacy;
        }

        public long getTrackUpdateTime() {
            return trackUpdateTime;
        }

        public void setTrackUpdateTime(long trackUpdateTime) {
            this.trackUpdateTime = trackUpdateTime;
        }

        public long getPlayCount() {
            return playCount;
        }

        public void setPlayCount(long playCount) {
            this.playCount = playCount;
        }

        public boolean isOrdered() {
            return ordered;
        }

        public void setOrdered(boolean ordered) {
            this.ordered = ordered;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCoverImgId_str() {
            return coverImgId_str;
        }

        public void setCoverImgId_str(String coverImgId_str) {
            this.coverImgId_str = coverImgId_str;
        }

        public String getToplistType() {
            return ToplistType;
        }

        public void setToplistType(String ToplistType) {
            this.ToplistType = ToplistType;
        }

        public List<?> getSubscribers() {
            return subscribers;
        }

        public void setSubscribers(List<?> subscribers) {
            this.subscribers = subscribers;
        }

        public List<?> getTags() {
            return tags;
        }

        public void setTags(List<?> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "subscribed=" + subscribed +
                    ", creator=" + creator +
                    ", artists=" + artists +
                    ", tracks=" + tracks +
                    ", updateFrequency='" + updateFrequency + '\'' +
                    ", backgroundCoverId=" + backgroundCoverId +
                    ", backgroundCoverUrl=" + backgroundCoverUrl +
                    ", titleImage=" + titleImage +
                    ", titleImageUrl=" + titleImageUrl +
                    ", englishTitle=" + englishTitle +
                    ", opRecommend=" + opRecommend +
                    ", recommendInfo=" + recommendInfo +
                    ", trackNumberUpdateTime=" + trackNumberUpdateTime +
                    ", adType=" + adType +
                    ", subscribedCount=" + subscribedCount +
                    ", cloudTrackCount=" + cloudTrackCount +
                    ", userId=" + userId +
                    ", createTime=" + createTime +
                    ", highQuality=" + highQuality +
                    ", updateTime=" + updateTime +
                    ", coverImgId=" + coverImgId +
                    ", newImported=" + newImported +
                    ", anonimous=" + anonimous +
                    ", specialType=" + specialType +
                    ", totalDuration=" + totalDuration +
                    ", coverImgUrl='" + coverImgUrl + '\'' +
                    ", trackCount=" + trackCount +
                    ", commentThreadId='" + commentThreadId + '\'' +
                    ", privacy=" + privacy +
                    ", trackUpdateTime=" + trackUpdateTime +
                    ", playCount=" + playCount +
                    ", ordered=" + ordered +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    ", name='" + name + '\'' +
                    ", id=" + id +
                    ", coverImgId_str='" + coverImgId_str + '\'' +
                    ", ToplistType='" + ToplistType + '\'' +
                    ", subscribers=" + subscribers +
                    ", tags=" + tags +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TopListEntity{" +
                "code=" + code +
                ", artistToplist=" + artistToplist +
                ", list=" + list +
                '}';
    }
}
