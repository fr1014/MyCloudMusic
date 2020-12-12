package com.fr1014.mycoludmusic.ui.home.toplist;

import android.view.View;

import com.fr1014.frecyclerviewadapter.BaseAdapter;
import com.fr1014.frecyclerviewadapter.BaseViewHolder;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.musicmanager.Music;

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
            holder.getView(R.id.tv_rank).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_rank, holder.getLayoutPosition() + "");
        }
        holder.setText(R.id.tv_song_name, data.getTitle());
        holder.setText(R.id.tv_author, data.getArtist());

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
}
