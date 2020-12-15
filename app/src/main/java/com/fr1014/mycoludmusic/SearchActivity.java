package com.fr1014.mycoludmusic;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.fr1014.frecyclerviewadapter.BaseAdapter;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.customview.PlayStatusBarView;
import com.fr1014.mycoludmusic.databinding.ActivitySearchBinding;
import com.fr1014.mycoludmusic.ui.home.toplist.PlayListDetailAdapter;
import com.fr1014.mycoludmusic.ui.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.utils.ScreenUtil;

import java.util.List;

public class SearchActivity extends BasePlayActivity<ActivitySearchBinding,TopListViewModel> {
    private PlayListDetailAdapter adapter;
    private PlayStatusBarView statusBarView;

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        int top = ScreenUtil.getStatusHeight(MyApplication.getInstance());
        mViewBinding.llSearch.setPadding(0, top, 0, 0);
    }

    @Override
    protected ActivitySearchBinding getViewBinding() {
        return mViewBinding = ActivitySearchBinding.inflate(getLayoutInflater());
    }

    @Override
    public TopListViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(this, factory).get(TopListViewModel.class);
    }

    @Override
    protected void initView() {
        initAdapter();
        initEditText();
        initSystemBar();
        initListener();
    }

    private void initListener() {
        mViewBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onServiceBound() {
        statusBarView = new PlayStatusBarView(this, getSupportFragmentManager());
        AudioPlayer.get().addOnPlayEventListener(statusBarView);
        mViewBinding.flPlaystatus.addView(statusBarView);
    }

    @Override
    public void initData() {
        mViewModel.getSearch().observe(this, new Observer<List<Music>>() {
            @Override
            public void onChanged(List<Music> music) {
                adapter.setData(music);
            }
        });

        mViewModel.getSongUrl().observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                if (!TextUtils.isEmpty(music.getSongUrl())) {
                    AudioPlayer.get().addAndPlay(music);
                } else {
                    AudioPlayer.get().playNext();
                }
            }
        });
    }

    private void initEditText() {
        mViewBinding.etKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) mViewBinding.etKeywords.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            SearchActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
//                    viewModel.getSearchEntityWYY(binding.etKeywords.getText().toString(), 0);
//                    viewModel.getSearchEntityKW(mViewBinding.etKeywords.getText().toString(), 30);
                    mViewModel.getSearchEntityKW(mViewBinding.etKeywords.getText().toString(), 0, 30);
                    return true;
                }
                return false;
            }
        });
    }

    private void initAdapter() {
        adapter = new PlayListDetailAdapter(false);
        adapter.setDisplayMarginView(true);
        mViewBinding.rvSearch.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.rvSearch.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, int position) {
//                viewModel.getSongUrlEntity((Music) adapter.getData(position));
//                viewModel.checkSong((Music) adapter.getData(position));
                mViewModel.getKWSongUrl((Music) adapter.getData(position));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (statusBarView != null) {
            AudioPlayer.get().removeOnPlayEventListener(statusBarView);
        }
    }
}