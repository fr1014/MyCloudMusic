package com.fr1014.mycoludmusic.ui.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.fr1014.mycoludmusic.R

/**
 * Create by fanrui on 2021/4/13
 * Describe: paging3 底部
 */
class FooterAdapter(private val retry: () -> Unit) : LoadStateAdapter<FooterAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeView: View = itemView.findViewById(R.id.view_divider)
        val progressBar: LottieAnimationView = itemView.findViewById(R.id.lav_loading)
        val loadingMsg: TextView = itemView.findViewById(R.id.tv_loading)
        val errorMsg: TextView = itemView.findViewById(R.id.tv_error)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_view, parent, false)
        val holder = LoadStateViewHolder(view)
        holder.placeView.visibility = View.VISIBLE
        holder.errorMsg.setOnClickListener {
            retry()
        }
        return holder
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.progressBar.isVisible = loadState is LoadState.Loading
        holder.loadingMsg.isVisible = loadState is LoadState.Loading
        if (loadState is LoadState.Error) {
            when (loadState.error) {
                is NullPointerException -> {
                    holder.errorMsg.apply {
                        text = holder.itemView.context.getString(R.string.finish)
                        isClickable = false
                    }
                }
                else -> {
                    holder.errorMsg.apply {
                        text = holder.itemView.context.getString(R.string.retry)
                        isClickable = true
                    }
                }
            }
        }
        holder.errorMsg.isVisible = loadState is LoadState.Error
    }
}