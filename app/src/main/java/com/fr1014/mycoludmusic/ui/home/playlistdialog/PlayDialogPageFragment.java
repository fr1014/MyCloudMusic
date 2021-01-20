package com.fr1014.mycoludmusic.ui.home.playlistdialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.data.source.local.room.DBManager;
import com.fr1014.mycoludmusic.databinding.FragmentPlaydialogpageBinding;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayEventAdapterListener;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayerEventListener;
import com.fr1014.mycoludmusic.rx.MyDisposableObserver;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.utils.CollectionUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Create by fanrui on 2020/12/17
 * Describe:
 */
public class PlayDialogPageFragment extends Fragment{
    private static String PAGE_TYPE = "page_type";
    private static final int PAGE_TYPE_HISTORY = 0; //历史播放
    private static final int PAGE_TYPE_CURRENT = 1; //当前播放
    private int pageType;
    private FragmentPlaydialogpageBinding binding;
    private PlayDialogAdapter playDialogAdapter;
    private int oldPosition = -1;  //当前播放音乐的位置
    private OnDialogListener dialogListener;
    private OnPlayerEventListener onPlayerEventListener;

    public PlayDialogPageFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageType = bundle.getInt(PAGE_TYPE);
        }
    }

    public static PlayDialogPageFragment getInstance(int position) {
        PlayDialogPageFragment dialogPageFragment = new PlayDialogPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_TYPE, position);
        dialogPageFragment.setArguments(bundle);
        return dialogPageFragment;
    }

    public void setDialogListener(OnDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaydialogpageBinding.inflate(getLayoutInflater());
        initHeader();
        initAdapter();
        binding.rvPlaylist.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        binding.rvPlaylist.setAdapter(playDialogAdapter);

        inPageTypeData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AudioPlayer.get().addOnPlayEventListener(onPlayerEventListener = new OnPlayEventAdapterListener() {
            @Override
            public void onChange(@NotNull Music music) {
                if (pageType == PAGE_TYPE_CURRENT) {
                    List<Music> musicList = AudioPlayer.get().getMusicList();
                    int position = musicList.indexOf(music);
                    setHeaderCount(musicList.size());
                    playDialogAdapter.setData(musicList);
                    if (oldPosition != position) {
                        playDialogAdapter.setCurrentMusic(music);
                        playDialogAdapter.notifyDataSetChanged();
                        oldPosition = position;
                    }
                }
            }
        });
    }

    private void inPageTypeData() {
        if (pageType == PAGE_TYPE_HISTORY) {
            //刷新adapter中的数据
            DBManager.get().getLocalMusicList(true).observe(getViewLifecycleOwner(), new Observer<List<MusicEntity>>() {
                @Override
                public void onChanged(List<MusicEntity> musicEntities) {
                    if (!CollectionUtils.isEmptyList(musicEntities)) {
                        Observable.just(musicEntities)
                                .compose(RxSchedulers.applyIO())
                                .map(new Function<List<MusicEntity>, List<Music>>() {
                                    @Override
                                    public List<Music> apply(@io.reactivex.annotations.NonNull List<MusicEntity> musicEntities) throws Exception {
                                        List<Music> musicList = new ArrayList<>();
                                        for (MusicEntity musicEntity : musicEntities) {
                                            musicList.add(new Music(musicEntity.getId(), musicEntity.getArtist(), musicEntity.getTitle(), "", musicEntity.getImgUrl(), musicEntity.getMusicRid(), musicEntity.getDuration()));
                                        }
                                        Collections.reverse(musicList);
                                        return musicList;
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new MyDisposableObserver<List<Music>>() {
                                    @Override
                                    public void onNext(@io.reactivex.annotations.NonNull List<Music> musicList) {
                                        setHeaderCount(musicList.size());
                                        playDialogAdapter.setData(musicList);
                                        playDialogAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                }
            });
        } else {
            List<Music> playList = AudioPlayer.get().getMusicList();
            if (playList != null) {
                playDialogAdapter.setData(playList);
                scrollToPosition();
                playDialogAdapter.notifyDataSetChanged();
            }
            playDialogAdapter.setCurrentMusic(AudioPlayer.get().getPlayMusic());
        }
    }

    public void scrollToPosition() {
        oldPosition = AudioPlayer.get().getPlayPosition();
        binding.rvPlaylist.scrollToPosition(oldPosition);
    }

    private void initAdapter() {
        playDialogAdapter = new PlayDialogAdapter();
        playDialogAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.ll_playlist:
                    if (pageType == PAGE_TYPE_CURRENT) {
                        if (oldPosition != position) {
                            Music item = (Music) adapter.getData(position);
                            AudioPlayer.get().addAndPlay(item);
                        } else {
                            //点击的为当前播放的歌曲
                            if (getActivity() instanceof BasePlayActivity) {
                                ((BasePlayActivity) getActivity()).showPlayingFragment();
                                dialogListener.dialogDismiss();
                            }
                        }
                    } else {
                        Music item = (Music) adapter.getData(position);
                        List<Music> musicList = AudioPlayer.get().getMusicList();
                        for (Music music : musicList) {
                            if (TextUtils.equals(music.getTitle(), item.getTitle()) && TextUtils.equals(music.getArtist(), item.getArtist())) {
                                AudioPlayer.get().addAndPlay(music);
                                return;
                            }
                        }
                        AudioPlayer.get().addAndPlay(item);
                        dialogListener.dialogDismiss();
                    }
                    break;
                case R.id.iv_del:
                    Music music = (Music) adapter.getData(position);
                    if (pageType == PAGE_TYPE_HISTORY) {
                        DBManager.get().getMusicEntityItem(music).observe(getViewLifecycleOwner(), new Observer<MusicEntity>() {
                            @Override
                            public void onChanged(MusicEntity musicEntity) {
                                if (musicEntity != null) {
                                    DBManager.get().delete(musicEntity);
                                }
                            }
                        });
                    } else {
                        int mPosition = AudioPlayer.get().getMusicList().indexOf(music);
                        AudioPlayer.get().delete(mPosition);
                        initHeader();
                        playDialogAdapter.setData(AudioPlayer.get().getMusicList());
                        playDialogAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        });
    }

    private void initHeader() {
        if (pageType == PAGE_TYPE_HISTORY) {
            binding.header.tvType.setText("历史播放");
        } else {
            binding.header.tvType.setText("当前播放");
            int count = AudioPlayer.get().getMusicList().size();
            setHeaderCount(count);
        }
    }

    private void setHeaderCount(int count) {
        binding.header.tvCount.setText(String.format("(%d)", count));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onPlayerEventListener != null){
            AudioPlayer.get().removeOnPlayEventListener(onPlayerEventListener);
        }
    }

    public interface OnDialogListener {
        void dialogDismiss();
    }
}
