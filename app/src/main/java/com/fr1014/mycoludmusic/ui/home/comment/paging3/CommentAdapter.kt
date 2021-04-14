package com.fr1014.mycoludmusic.ui.home.comment.paging3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.comment.CommentItem
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel
import com.fr1014.mycoludmusic.utils.CommonUtils
import com.fr1014.mycoludmusic.utils.glide.GlideApp
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Create by fanrui on 2021/4/11
 * Describe:
 */
class CommentAdapter : PagingDataAdapter<CommentItem, CommentAdapter.CommentViewHolder>(CommentItemComparator), View.OnClickListener {

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
        val tvGoodCount: TextView = itemView.findViewById(R.id.count_good)
        val ivGood: ImageView = itemView.findViewById(R.id.iv_good)
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
            holder.tvGoodCount.text = comment.likedCount.toString()
            holder.reply.apply {
                visibility = if (comment.showFloorComment.showReplyCount) {
                    text = "${comment.showFloorComment.replyCount}条回复 >"
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        holder.apply {
            reply.setOnClickListener(this@CommentAdapter)
            comment.setOnClickListener(this@CommentAdapter)
            clHead.setOnClickListener(this@CommentAdapter)
            tvGoodCount.setOnClickListener(this@CommentAdapter)
            ivGood.setOnClickListener(this@CommentAdapter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_good, R.id.count_good -> { //点赞

            }
            R.id.reply -> {  //回复
                CommonUtils.rd_ing()
            }
            R.id.cl_head, R.id.nick_name -> {  //用户主页
                CommonUtils.rd_ing()
            }
            R.id.comment ->{  //评论
                CommonUtils.rd_ing()
            }
        }
    }
}