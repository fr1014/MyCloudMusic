package com.fr1014.mycoludmusic.ui.home.homepage.dayrecommend

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean
import com.fr1014.mycoludmusic.databinding.FragmentDayRecommendBinding
import com.fr1014.mycoludmusic.musicmanager.player.*
import com.fr1014.mycoludmusic.utils.BlurImageUtils
import com.fr1014.mycoludmusic.utils.ScreenUtils
import com.fr1014.mycoludmusic.utils.StatusBarUtils
import com.fr1014.mycoludmusic.utils.glide.GlideApp
import com.fr1014.mymvvm.base.BaseFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList

class DayRecommendFragment : BaseFragment<FragmentDayRecommendBinding, DayRecommendViewModel>() {
    private lateinit var mAdapter: DayRecommendAdapter
    private lateinit var mHeaderView: View
    private var headerHeight = 0
    private var headerBitmap: Bitmap? = null

    override fun initViewModel(): DayRecommendViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(DayRecommendViewModel::class.java)
    }

    override fun getViewBinding(container: ViewGroup?): FragmentDayRecommendBinding {
        return FragmentDayRecommendBinding.inflate(layoutInflater)
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        activity?.let {
            StatusBarUtils.setImmersiveStatusBar(it.window, false)
        }
        mAdapter = DayRecommendAdapter(R.layout.item_day_recommend)
        mViewBinding.rvDayRecommend.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            mHeaderView = layoutInflater.inflate(R.layout.header_day_recommend, this, false)
            mAdapter.headerView = mHeaderView
        }
        initListener()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayerEvent(event: PlayerEvent){
        when(event.type){
            PlayerType.OnChange -> {
                Log.d("hello", "onPlayerEvent: " + event.music?.title)
            }
            else ->{

            }
        }
    }

    private fun initListener() {
        mViewBinding.apply {
            ivBack.setOnClickListener {
                Navigation.findNavController(it).popBackStack()
            }

            playAll.llPlaylist.setOnClickListener {
                mAdapter.datas?.let {
                    MyAudioPlay.get().initPlayList(it.getMusics()).play()
                }
            }

            rvDayRecommend.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val scrollY = getScrollY()
                    ivBg.alpha = scrollY.toFloat() / ScreenUtils.dp2px(146f)
                    tvTitle.text = if (scrollY > ScreenUtils.dp2px(146f)) "每日推荐" else ""
                    playAll.llPlaylist.visibility = if (scrollY > ScreenUtils.dp2px(146f)) View.VISIBLE else View.GONE
                }
            })
        }
    }

    private fun getScrollY(): Int {
        val layoutManager = mViewBinding.rvDayRecommend.layoutManager as LinearLayoutManager
        // 获取第一个可见item的位置
        val position = layoutManager.findFirstVisibleItemPosition()
        if (position == 0) {
            // 获取header
            val headerView = layoutManager.findViewByPosition(0)
            // 获取第一个可见item的高度
            headerHeight = headerView!!.height
        }

        // 获取第一个可见item
        val firstVisiableChildView = layoutManager.findViewByPosition(position)
        // 获取第一个可见item的高度
        val itemHeight = firstVisiableChildView!!.height
        // 获取第一个可见item的位置
        return if (position == 0) {
            position * itemHeight - firstVisiableChildView.top
        } else {
            position * itemHeight - firstVisiableChildView.top + headerHeight
        }
    }

    override fun initData() {
        mViewModel.getDayRecommend()
        mHeaderView.findViewById<TextView>(R.id.tv_day).text = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
        mHeaderView.findViewById<TextView>(R.id.tv_month).text = getMonth()
    }

    private fun getMonth(): String {
        val month = Calendar.getInstance().get(Calendar.MONTH).plus(1)
        return if (month < 10) " /0${month}" else " /$month"
    }

    override fun initViewObservable() {
        mViewModel.getDayRecommendData().observe(this, Observer { recommend ->
            recommend.data.dailySongs.apply {
                if (isNotEmpty()) {
                    GlideApp.with(this@DayRecommendFragment)
                            .asBitmap()
                            .load(recommend.data.dailySongs[0].al.picUrl)
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    headerBitmap = resource
                                    mHeaderView.findViewById<ImageView>(R.id.iv_head).setImageBitmap(resource)
                                    mViewBinding.ivBg.setImageDrawable(BlurImageUtils.getForegroundDrawable(context, resource, 5))
                                }

                            })
                    mAdapter.setData(this)
                    mAdapter.datas.size.toString().apply {
                        mViewBinding.playAll.tvCount.text = this
                        mHeaderView.findViewById<TextView>(R.id.tv_count).text = this
                    }
                    mHeaderView.findViewById<LinearLayout>(R.id.play_all).setOnClickListener {
                        MyAudioPlay.get().initPlayList(this.getMusics()).play()
                    }
                }
            }
        })
    }
}

fun List<SongsBean>.getMusics(): List<Music> {
    val musics: MutableList<Music> = ArrayList()
    for (bean in this) {
        val sb = StringBuilder()
        for (i in bean.ar.indices) {
            bean.ar[i].apply {
                sb.append(name).append('/')
            }
        }
        val music = Music(bean.id.toString(), sb.substring(0, sb.length - 1), bean.name, bean.mv.toString(), MusicSource.WY_MUSIC.sourceType)
        musics.add(music)
    }
    return musics
}