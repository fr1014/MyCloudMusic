package com.fr1014.mycoludmusic.home.toplist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.FragmentTopListBinding;
import com.fr1014.mycoludmusic.entity.wangyiyun.TopListDetailEntity;


//排行榜榜单页面
public class TopListFragment extends Fragment {
    private TopListViewModel viewModel;
    private TopListAdapter topListAdapter;


    public TopListFragment() {
        // Required empty public constructor
    }


    public static TopListFragment newInstance(String param1, String param2) {
        TopListFragment fragment = new TopListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTopListBinding binding = FragmentTopListBinding.inflate(getLayoutInflater(),container,false);


        //使用自定义的factory,初始化 new AppViewModelFactory(application,MyApplication.provideRepository()) 带网络数据的ViewModel
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        viewModel = new ViewModelProvider(getActivity(), factory).get(TopListViewModel.class);

        initAdapter();

        viewModel.getTopListDetail().observe(getViewLifecycleOwner(), new Observer<TopListDetailEntity>() {
            @Override
            public void onChanged(TopListDetailEntity topListDetailEntity) {
                topListAdapter.setData(topListDetailEntity.getList());
            }
        });
        binding.rvTopList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.rvTopList.setAdapter(topListAdapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initAdapter() {
        topListAdapter = new TopListAdapter();
        topListAdapter.setOnItemClickListener(topListAdapter);
    }
}