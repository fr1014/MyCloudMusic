package com.fr1014.mycoludmusic.home.toplist;

import android.util.Log;
import android.view.View;

import com.fr1014.frecyclerviewadapter.BaseAdapter;
import com.fr1014.frecyclerviewadapter.BaseViewHolder;
import com.fr1014.musicmanager.Music;
import com.fr1014.mycoludmusic.R;

/**
 * 创建时间:2020/9/5
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class PlayListDetailAdapter extends BaseAdapter<Music, BaseViewHolder> {
    //默认显示序号
    private boolean isisDisplaySeq = true;

    /**
     * @param isDisplaySeq 是否显示序号
     */
    public PlayListDetailAdapter(boolean isDisplaySeq) {
        this();
        this.isisDisplaySeq = isDisplaySeq;
    }

    public PlayListDetailAdapter() {
        super(R.layout.item_playlist_detail, null);
    }

    private static final String TAG = "PlayListDetailAdapter";

    @Override
    protected void convert(BaseViewHolder holder, Music data) {
        if (isisDisplaySeq) {
            holder.getView(R.id.tv_rank).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_rank, holder.getLayoutPosition() + "");
        }
        holder.setText(R.id.tv_song_name, data.getTitle());
        holder.setText(R.id.tv_author, data.getArtist());
    }
}
