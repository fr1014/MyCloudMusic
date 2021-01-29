package com.fr1014.mycoludmusic.ui.home.homepage.dayrecommend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.databinding.FragmentDayRecommendBinding
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.utils.glide.GlideApp
import com.fr1014.mymvvm.base.BaseFragment

class DayRecommendFragment : BaseFragment<FragmentDayRecommendBinding, DayRecommendViewModel>() {
    private lateinit var mAdapter: DayRecommendAdapter
    private lateinit var mHeaderView: View

    override fun initViewModel(): DayRecommendViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(DayRecommendViewModel::class.java)
    }

    override fun getViewBinding(container: ViewGroup?): FragmentDayRecommendBinding {
        return FragmentDayRecommendBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mAdapter = DayRecommendAdapter(R.layout.item_day_recommend)
        mViewBinding.rvDayRecommend.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            mHeaderView = layoutInflater.inflate(R.layout.header_day_recommend, this, false)
            mAdapter.headerView = mHeaderView
        }
    }

    override fun initData() {
        mViewModel.getDayRecommend()
    }

    override fun initViewObservable() {
        mViewModel.getDayRecommendData().observe(this, Observer { recommend ->
            recommend.data.dailySongs.apply {
                if (isNotEmpty()) {
                    GlideApp.with(this@DayRecommendFragment)
                            .load(recommend.data.dailySongs[0].al.picUrl)
                            .centerCrop()
                            .into(mHeaderView.findViewById(R.id.iv_head))
                    mAdapter.setData(this)
                    val musics: MutableList<Music> = ArrayList()
                    for (bean in this) {
                        val sb = StringBuilder()
                        for (i in bean.ar.indices) {
                            bean.ar[i].apply {
                                sb.append(name).append('/')
                            }
                        }
                        val music = Music(bean.id,sb.substring(0,sb.length-1),bean.name,"","","")
                        musics.add(music)
                    }
                    mHeaderView.findViewById<LinearLayout>(R.id.play_all).setOnClickListener {
                        AudioPlayer.get().addAndPlay(musics)
                    }
                }
            }
        })
    }
}