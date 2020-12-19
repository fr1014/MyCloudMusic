package com.fr1014.mycoludmusic.ui.home.toplist;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fr1014.frecyclerviewadapter.BaseAdapter;
import com.fr1014.frecyclerviewadapter.BaseViewHolder;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.utils.ScreenUtil;

/**
 * 创建时间:2020/9/5
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class PlayListDetailAdapter extends BaseAdapter<Music, BaseViewHolder> {
    //默认显示序号
    private boolean isDisplaySeq = true;

    //是否显示底部的View
    private boolean isDisplayMarginView = false;

    /**
     * @param isDisplaySeq 是否显示序号
     */
    public PlayListDetailAdapter(boolean isDisplaySeq) {
        this();
        this.isDisplaySeq = isDisplaySeq;
    }

    public PlayListDetailAdapter() {
        super(R.layout.item_playlist_detail, null);
    }

    private static final String TAG = "PlayListDetailAdapter";

    @Override
    protected void convert(BaseViewHolder holder, Music data) {
        if (isDisplaySeq) {
            holder.getView(R.id.cl_status).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_rank, holder.getLayoutPosition() + "");
        } else {
            holder.getView(R.id.cl_status).setVisibility(View.GONE);
        }
        holder.setText(R.id.tv_song_name, data.getTitle());

        //歌手 - 专辑
        if (TextUtils.isEmpty(data.getAlbum())) {
            holder.setText(R.id.tv_author, data.getArtist());
        } else {
            holder.setText(R.id.tv_author, data.getArtist() + " - " + data.getAlbum());
        }

        //是否为原唱
        if (TextUtils.equals(data.getOriginal(), "1")) {
            holder.getView(R.id.tv_prompt).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_prompt, mContext.getString(R.string.original));
            setAuthorLeftMargin(holder, 4);
        } else {
            holder.getView(R.id.tv_prompt).setVisibility(View.GONE);
            setAuthorLeftMargin(holder, 0);
        }

        //别名/介绍
        if (TextUtils.isEmpty(data.getSubTitle())) {
            holder.getView(R.id.tv_subtitle).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_subtitle).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_subtitle, data.getSubTitle());
        }

        //是否显示最底部的MarginView
        if (isDisplayMarginView) {
            holder.getView(R.id.margin_barsize).setVisibility(isShowDivider(holder) ? View.VISIBLE : View.GONE);
        }

    }

    private boolean isShowDivider(BaseViewHolder holder) {
//        Log.d(TAG, "----isShowDivider1: " + (holder.getLayoutPosition() == getDatas().size() - 1));
//        Log.d(TAG, "----isShowDivider2: " + "holder.getLayoutPosition():" + holder.getLayoutPosition());
//        Log.d(TAG, "----isShowDivider3: " + "getDatas().size() - 1:" + (getDatas().size() - 1));
        return getRealPosition(holder) == getDatas().size() - 1;
    }

    public void setDisplayMarginView(boolean displayMarginView) {
        isDisplayMarginView = displayMarginView;
    }

    private void setAuthorLeftMargin(BaseViewHolder holder, int dp) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getView(R.id.tv_author).getLayoutParams();
        layoutParams.leftMargin = ScreenUtil.dp2px(mContext, dp);
        holder.getView(R.id.tv_author).setLayoutParams(layoutParams);
    }
}
