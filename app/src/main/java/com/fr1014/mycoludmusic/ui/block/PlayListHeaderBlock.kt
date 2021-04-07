package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Playlist
import com.fr1014.mycoludmusic.databinding.BlockPlaylistHeaderBinding
import com.fr1014.mycoludmusic.ui.home.playlist.PlayListViewModel
import com.fr1014.mycoludmusic.utils.CommonUtils

/**
 * Create by fanrui on 2021/4/3
 * Describe: 歌单头部：收藏，评论，分享
 */
class PlayListHeaderBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private lateinit var mViewBinding: BlockPlaylistHeaderBinding
    private var mViewModel: PlayListViewModel? = null
    var playList: Playlist? = null

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockPlaylistHeaderBinding.inflate(LayoutInflater.from(context), this)

        mViewBinding.apply {
            ivCollection.setOnClickListener(this@PlayListHeaderBlock)
            tvCollection.setOnClickListener(this@PlayListHeaderBlock)
            ivComment.setOnClickListener(this@PlayListHeaderBlock)
            tvComment.setOnClickListener(this@PlayListHeaderBlock)
            ivShare.setOnClickListener(this@PlayListHeaderBlock)
            tvShare.setOnClickListener(this@PlayListHeaderBlock)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_collection, R.id.tv_collection -> {
                playList?.let { mViewModel?.collectPlayList(it.id) }
            }
            R.id.iv_comment, R.id.tv_comment -> {
                tips()
            }
            R.id.iv_share, R.id.tv_share -> {
                tips()
            }
        }
    }

    private fun tips() {
        CommonUtils.toastShort(context.getString(R.string.dev))
    }

    fun setData(playListDetailEntity: PlayListDetailEntity?, viewModel: PlayListViewModel) {
        mViewModel = viewModel
        mViewBinding.apply {
            playListDetailEntity?.playlist?.let {
                playList = it;

                if (it.subscribedCount != 0L) {
                    tvCollection.text = CommonUtils.formatNumber(it.subscribedCount)
                }
                if (it.commentCount != 0L) {
                    tvComment.text = CommonUtils.formatNumber(it.commentCount)
                }
                if (it.shareCount != 0L) {
                    tvShare.text = CommonUtils.formatNumber(it.shareCount)
                }
            }
        }

    }

    fun setData(type: Int) {
        mViewBinding.apply {
            ivCollection.setImageResource(if (type == 1) R.drawable.ic_collection else R.drawable.ic_collected)
        }

    }

//    private fun initViewModelObserver() {
//        mViewModel?.apply {
//            getCollectPlayList().observe(this@PlayListHeaderBlock, Observer {
//
//            })
//        }
//    }
}