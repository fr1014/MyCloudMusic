package com.fr1014.mycoludmusic.ui.block

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.fr1014.mycoludmusic.databinding.BlockTopListBinding

class TopListBlock @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewBinding : BlockTopListBinding;

    init {
        initView()
    }

    private fun initView() {
        mViewBinding = BlockTopListBinding.inflate(LayoutInflater.from(context),this,false)
        addView(mViewBinding.root)
    }

}