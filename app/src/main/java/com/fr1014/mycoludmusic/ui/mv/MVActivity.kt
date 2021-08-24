package com.fr1014.mycoludmusic.ui.mv

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fr1014.mycoludmusic.databinding.ActivityMvBinding
import com.fr1014.mycoludmusic.utils.StatusBarUtils

const val MV_TITLE = "mv_title"
const val MV_URL = "mv_url"
const val MV_FIRST_PLAY = "mv_first_play"
const val MV = "mv"

class MVActivity : AppCompatActivity() {

    lateinit var mViewBinding: ActivityMvBinding

    companion object {
        fun startMVActivity(context: Context, url: String, title: String?, isFirstPlay: Boolean) {
            val intent = Intent(context, MVActivity::class.java)
            intent.putExtra(MV, Bundle().apply {
                putString(MV_URL, url)
                putString(MV_TITLE, title)
                putBoolean(MV_FIRST_PLAY, isFirstPlay)
            })
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMvBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        StatusBarUtils.setImmersiveStatusBar(window,false)

        intent?.getBundleExtra(MV)?.let {
            val url = it.getString(MV_URL, "")
            val title = it.getString(MV_TITLE, "")
            val firstPlay = it.getBoolean(MV_FIRST_PLAY)
            mViewBinding.frVideoPlayer.apply {
                lifecycle.addObserver(this)
                isFirstPlay = firstPlay
                setDataSource(url, title)
            }
        }
    }
}