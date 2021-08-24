package com.fr1014.mycoludmusic.ui.home.playlistdialog;

import com.fr1014.frecyclerviewadapter.BaseAdapter;
import com.fr1014.frecyclerviewadapter.BaseViewHolder;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.musicmanager.player.Music;
import com.fr1014.mycoludmusic.musicmanager.player.MusicKt;

/**
 * 创建时间:2020/9/15
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class PlayDialogAdapter extends BaseAdapter<Music, BaseViewHolder> {
    private Music currentMusic = null;

    public PlayDialogAdapter() {
        super(R.layout.item_playlist, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, Music data) {
        holder.addOnClickListener(R.id.ll_playlist, R.id.iv_del);
        if (currentMusic != null && MusicKt.isSameMusic(currentMusic, data)) {
            holder.setText(R.id.tv_song_name, data.getTitle()).setTextColor(R.id.tv_song_name, MyApplication.getInstance().getResources().getColor(R.color.red));
            holder.setText(R.id.tv_author, "- " + data.getArtist()).setTextColor(R.id.tv_author, MyApplication.getInstance().getResources().getColor(R.color.red));
        } else {
            holder.setText(R.id.tv_song_name, data.getTitle()).setTextColor(R.id.tv_song_name, MyApplication.getInstance().getResources().getColor(R.color.black));
            holder.setText(R.id.tv_author, "- " + data.getArtist()).setTextColor(R.id.tv_author, MyApplication.getInstance().getResources().getColor(R.color.light_black));
        }
    }

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
    }
}
