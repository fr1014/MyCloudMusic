package com.fr1014.mycoludmusic.ui.home.comment.paging3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.CommentItem
import com.fr1014.mycoludmusic.utils.glide.GlideApp
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Create by fanrui on 2021/4/11
 * Describe:
 */
class CommentAdapter : PagingDataAdapter<CommentItem, CommentAdapter.CommentViewHolder>(CommentItemComparator) {

    object CommentItemComparator : DiffUtil.ItemCallback<CommentItem>() {
        override fun areItemsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
            return oldItem == newItem
        }

    }

    class CommentViewHolder(view: View) : BaseViewHolder(view) {

    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentItem = getItem(position)
        val clHead = holder.itemView.findViewById<CircleImageView>(R.id.cl_head)
        GlideApp.with(clHead)
                .load(commentItem?.user?.avatarUrl)
                .into(clHead)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))
    }
}