package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.fr1014.mycoludmusic.MainViewModel
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppCache
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.Profile
import com.fr1014.mycoludmusic.databinding.BlockMynavigationviewBinding
import com.fr1014.mycoludmusic.musicmanager.PlayService
import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.musicmanager.QuitTimer
import com.fr1014.mycoludmusic.musicmanager.constants.Actions
import com.fr1014.mycoludmusic.utils.CommonUtils

class MyNavigationView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), QuitTimer.OnTimerListener {

    private lateinit var mViewBinding: BlockMynavigationviewBinding
    private var mViewModel: MainViewModel? = null
    private var owner: LifecycleOwner? = null
    private var toast: Toast? = null

    init {
        initView()
    }

    fun setViewModel(mViewModel: MainViewModel, owner: LifecycleOwner) {
        this.mViewModel = mViewModel
        this.owner = owner
        initViewModelObserver()
    }

    private fun initViewModelObserver() {
        owner?.let {
            mViewModel?.getLogoutLive()?.observe(it, Observer { logout: Boolean ->
                if (logout) {
                    Preferences.saveUserProfile(null)
                    Preferences.saveCookie(null)
                    CommonUtils.toastShort("啊啊啊啊！居然退出登录啦，你怎么敢的啊！")
                    AppCache.get().clearStack()
                    PlayService.startCommand(context, Actions.ACTION_STOP)
                } else {
                    CommonUtils.toastShort("遇到了有意思的事情开小差了，请稍后重试")
                }
            })
        }
    }

    private fun initView() {
        mViewBinding = BlockMynavigationviewBinding.inflate(LayoutInflater.from(context), this)
        initClick()
    }

    private fun initClick() {
        mViewBinding.apply {
            tvLogout.setOnClickListener {
                val profile = Preferences.getUserProfile()
                if (profile != null) {
                    mViewModel?.logout();
                } else {
                    CommonUtils.toastShort("尚未登录你咋退出啊？")
                }
            }
            includeTiming.clTiming.setOnClickListener {
                timerDialog()
            }
        }
    }

    private fun timerDialog() {
        AlertDialog.Builder(context)
                .setTitle(R.string.menu_timer)
                .setItems(context.resources.getStringArray(R.array.timer_text)) { dialog, which ->
                    val times: IntArray = context.resources.getIntArray(R.array.timer_int)
                    startTimer(times[which])
                }
                .show()
    }

    private fun startTimer(minute: Int) {
        QuitTimer.get().start((minute * 60 * 1000).toLong())
        toast?.cancel()
        toast = if (minute > 0) {
            CommonUtils.toastShort(context.getString(R.string.timer_set, minute.toString()))
        } else {
            CommonUtils.toastShort(context.getString(R.string.timer_cancel))
        }
    }

    fun initNavHeaderViewData(profile: Profile?) {
        mViewBinding.includeHeader.apply {
            profile?.let {
                Glide.with(avatar)
                        .load(profile.avatarUrl)
                        .circleCrop()
                        .into(avatar)
                userNike.text = it.nickname
            }
        }
    }

    override fun onTimer(remain: Long) {
        val title: String = context.getString(R.string.menu_timer)
        mViewBinding.includeTiming.tvItem.text = if (remain == 0L) title else CommonUtils.formatTime("$title(mm:ss)", remain)
    }
}