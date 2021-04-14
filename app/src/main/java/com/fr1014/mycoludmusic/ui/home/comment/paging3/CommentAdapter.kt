package com.fr1014.mycoludmusic.ui.home.comment.paging3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.CommentItem
import com.fr1014.mycoludmusic.utils.CommonUtils
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
        val clHead: CircleImageView = itemView.findViewById(R.id.cl_head)
        val nickName: TextView = itemView.findViewById(R.id.nick_name)
        val time: TextView = itemView.findViewById(R.id.time)
        val comment: TextView = itemView.findViewById(R.id.comment)
        val reply: TextView = itemView.findViewById(R.id.reply)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentItem = getItem(position)
        commentItem?.let { comment ->
            GlideApp.with(holder.clHead)
                    .load(comment.user.avatarUrl)
                    .placeholder(R.drawable.place_head)
                    .into(holder.clHead)
            holder.nickName.text = comment.user.nickname
            holder.time.text = CommonUtils.formatTimeYear(comment.time)
            holder.comment.text = comment.content
            holder.reply.apply {
                visibility = if (comment.showFloorComment.showReplyCount) {
                    text = "${comment.showFloorComment.replyCount}条回复 >"
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))
    }
}