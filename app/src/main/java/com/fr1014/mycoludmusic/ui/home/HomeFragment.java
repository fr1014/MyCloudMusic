package com.fr1014.mycoludmusic.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.fr1014.mycoludmusic.MainActivity;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.RecommendPlayList;
import com.fr1014.mycoludmusic.databinding.FragmentHomeBinding;
import com.fr1014.mycoludmusic.ui.login.LoginActivity;
import com.fr1014.mymvvm.base.BaseFragment;

public class HomeFragment extends BaseFragment<FragmentHomeBinding,HomeViewModel> {

    @Override
    protected FragmentHomeBinding getViewBinding(ViewGroup container) {
        return FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected HomeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(MyApplication.getInstance());
        return new ViewModelProvider(getActivity(),factory).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        initRecommendPlayList();
        initListener();
    }

    private void initRecommendPlayList() {
        mViewBinding.blockRecommend.setTitle("推荐歌单");
    }

    @Override
    public void initData() {
        mViewModel.getWYRecommendList(30);
    }

    @Override
    public void initViewObservable() {
        mViewModel.getRecommendListLiveData().observe(this, new Observer<RecommendPlayList>() {
            @Override
            public void onChanged(RecommendPlayList recommendPlayList) {
                mViewBinding.blockRecommend.bindData(recommendPlayList.getResult());
            }
        });
    }

    private void initListener() {
        mViewBinding.btToplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), TopListActivity.class);
//                startActivity(intent);
                Navigation.findNavController(v).navigate(R.id.topListFragment);
            }
        });

        mViewBinding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
//                Profile userProfile = Preferences.getUserProfile();
//                if (userProfile != null){
//                    Log.d("hello", "onClick: "+userProfile.toString());
//                }
            }
        });

        mViewBinding.btLikeList.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.userInfoFragment);
        });
    }

}