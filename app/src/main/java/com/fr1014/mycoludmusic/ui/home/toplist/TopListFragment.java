package com.fr1014.mycoludmusic.ui.home.toplist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.ViewGroup;

import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.FragmentTopListBinding;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mymvvm.base.BaseFragment;

//排行榜榜单页面
public class TopListFragment extends BaseFragment<FragmentTopListBinding, TopListViewModel> {
    private TopListAdapter topListAdapter;


    public TopListFragment() {
        // Required empty public constructor
    }

    public static TopListFragment newInstance(String param1, String param2) {
        TopListFragment fragment = new TopListFragment();
        return fragment;
    }

    @Override
    protected FragmentTopListBinding getViewBinding(ViewGroup container) {
        return FragmentTopListBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected TopListViewModel initViewModel() {
        //使用自定义的factory,初始化 new AppViewModelFactory(application,MyApplication.provideRepository()) 带网络数据的ViewModel
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(getActivity(), factory).get(TopListViewModel.class);
    }

    @Override
    protected void initView() {
        topListAdapter = new TopListAdapter();
        mViewBinding.rvTopList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mViewBinding.rvTopList.setAdapter(topListAdapter);
    }

    @Override
    public void initViewObservable() {
        mViewModel.getTopListDetail().observe(getViewLifecycleOwner(), new Observer<TopListDetailEntity>() {
            @Override
            public void onChanged(TopListDetailEntity topListDetailEntity) {
                topListAdapter.setData(topListDetailEntity.getList());
            }
        });
    }
}