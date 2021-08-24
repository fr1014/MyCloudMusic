package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.fr1014.mycoludmusic.MainActivity
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.BaseConfig
import com.fr1014.mycoludmusic.databinding.BlockHomeClickBinding
import com.fr1014.mycoludmusic.ui.login.LoginActivity
import com.fr1014.mycoludmusic.utils.CommonUtils
import java.util.*

class HomeTopClickBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewBinding: BlockHomeClickBinding

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockHomeClickBinding.inflate(LayoutInflater.from(context), this)

        mViewBinding.apply {
            tvDate.text = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()

            ivDayRecommend.setOnClickListener {
                navigateTo {
                    Navigation.findNavController(it).navigate(R.id.action_nav_home_to_dayRecommendFragment)
                }
            }

            ivLike.setOnClickListener {
                navigateTo {
                    Navigation.findNavController(it).navigate(R.id.action_nav_home_to_userInfoFragment)
                }
            }

            ivTop.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_nav_home_to_topListFragment)
            }

            ivSongSale.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_nav_home_to_songSaleFragment)
            }
        }
    }

    private fun navigateTo(navigate: () -> Unit) {
        if (BaseConfig.isLogin) {
            navigate()
        } else {
            if (context is MainActivity) {
                CommonUtils.toastShort(context.getString(R.string.tips_login))
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        }
    }
}