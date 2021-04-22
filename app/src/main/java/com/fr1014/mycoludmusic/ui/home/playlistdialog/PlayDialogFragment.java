package com.fr1014.mycoludmusic.ui.home.playlistdialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.databinding.FragmentPlayListDialogBinding;
import com.fr1014.mycoludmusic.utils.ScreenUtils;

import java.util.HashMap;
import java.util.Map;


public class PlayDialogFragment extends DialogFragment implements PlayDialogPageFragment.OnDialogListener {

    private FragmentPlayListDialogBinding binding;
    private static final int NUM_PAGES = 2;
    private FragmentStateAdapter pagerAdapter;

    public PlayDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.PayListDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置content前设定
        dialog.setCanceledOnTouchOutside(true);  //外部点击取消
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getDialog().getWindow() != null) {
            //背景透明
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //背景蒙层
            getDialog().getWindow().setDimAmount(0.7f);
        }

        binding = FragmentPlayListDialogBinding.inflate(inflater, container, false);
        pagerAdapter = new PlayDialogPagerAdapter(getActivity());
        //设置Transformer
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(8));//添加边距Transformer
        compositePageTransformer.addTransformer(new ZoomOutPageTransformer());//设置滑动动画
        binding.pager.setPageTransformer(compositePageTransformer);
        //设置滚动方向
        binding.pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        //一屏多页
        View recyclerView = binding.pager.getChildAt(0);
        if (recyclerView instanceof RecyclerView) {
            recyclerView.setPadding(50, 0, 50, 0);
            ((RecyclerView) recyclerView).setClipToPadding(false);
        }
        binding.pager.setAdapter(pagerAdapter);
        binding.pager.setCurrentItem(1, false);
        return binding.getRoot();
    }

    /**
     * 修改布局的大小
     */
    @Override
    public void onStart() {
        super.onStart();

        resizeDialogFragment();

        PlayDialogPageFragment fragment = (PlayDialogPageFragment) pageFragments.get(1);
        if (fragment != null){
            fragment.initPlayMode();
            fragment.scrollToPosition();
        }
    }

    private void resizeDialogFragment() {
        Dialog dialog = getDialog();
        if (null != dialog) {
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.height = (2 * ScreenUtils.getScreenHeight() / 3);//获取屏幕的宽度，定义自己的宽度
                lp.width = ScreenUtils.getScreenWidth();
                lp.y = 0;
                lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                window.setLayout(lp.width, lp.height);
            }

        }
    }

    @Override
    public void dialogDismiss() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }

    private class PlayDialogPagerAdapter extends FragmentStateAdapter {
        public PlayDialogPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            PlayDialogPageFragment playDialogPageFragment = PlayDialogPageFragment.getInstance(position);
            playDialogPageFragment.setDialogListener(PlayDialogFragment.this);
            manageFragments(playDialogPageFragment, position);
            return playDialogPageFragment;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    public Map<Integer, Fragment> pageFragments = new HashMap<>();

    private void manageFragments(Fragment fragment, int position) {
        pageFragments.put(position, fragment);
    }
}