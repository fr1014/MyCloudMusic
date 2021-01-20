package com.fr1014.mycoludmusic.ui.home.playlist;

import android.text.TextUtils;
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

    //是否显示底部的View
    private boolean isDisplayMarginView = false;

    public PlayListDetailAdapter() {
        super(R.layout.item_playlist_detail, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, Music data) {
        holder.getView(R.id.cl_status).setVisibility(View.VISIBLE);
        holder.setText(R.id.tv_rank, holder.getLayoutPosition() + "");
        holder.setText(R.id.tv_song_name, data.getTitle());

        //歌手 - 专辑
        if (TextUtils.isEmpty(data.getAlbum())) {
            holder.setText(R.id.tv_author, data.getArtist());
        } else {
            holder.setText(R.id.tv_author, data.getArtist() + " - " + data.getAlbum());
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
}
