package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.fr1014.mycoludmusic.databinding.CustomUserControlbarBinding

class UserControlBarBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewBinding: CustomUserControlbarBinding

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = CustomUserControlbarBinding.inflate(LayoutInflater.from(context),this,false)
        addView(mViewBinding.root)
    }
}