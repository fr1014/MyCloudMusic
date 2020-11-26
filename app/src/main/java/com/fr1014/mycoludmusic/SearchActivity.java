package com.fr1014.mycoludmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.fr1014.frecyclerviewadapter.BaseAdapter;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.ActivitySearchBinding;
import com.fr1014.mycoludmusic.home.toplist.PlayListDetailAdapter;
import com.fr1014.mycoludmusic.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.MusicService;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private TopListViewModel viewModel;
    private PlayListDetailAdapter adapter;
    private MusicService.MusicControl musicControl;

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        viewModel = new ViewModelProvider(this, factory).get(TopListViewModel.class);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAdapter();

        initService();

        viewModel.getSearch().observe(this, new Observer<List<Music>>() {
            @Override
            public void onChanged(List<Music> music) {
                adapter.setData(music);
            }
        });

//        viewModel.getSearchEntityWYY("陈奕迅", 0);

        viewModel.getSongUrl().observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                musicControl.addPlayList(music);
            }
        });

        initEditText();
    }

    private void initEditText() {
        binding.etKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) binding.etKeywords.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            SearchActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
//                    viewModel.getSearchEntityWYY(binding.etKeywords.getText().toString(), 0);
                    viewModel.getSearchEntityKW(binding.etKeywords.getText().toString(), 0,10);
                    return true;
                }
                return false;
            }
        });
    }

    private void initService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initAdapter() {
        adapter = new PlayListDetailAdapter(false);
        binding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearch.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, int position) {
//                viewModel.getSongUrlEntity((Music) adapter.getData(position));
//                viewModel.checkSong((Music) adapter.getData(position));
                viewModel.getSongUrl((Music) adapter.getData(position));
            }
        });
    }
}