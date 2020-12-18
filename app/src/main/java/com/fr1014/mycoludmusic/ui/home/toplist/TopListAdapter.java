package com.fr1014.mycoludmusic.ui.home.toplist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fr1014.frecyclerviewadapter.BaseAdapter;
import com.fr1014.frecyclerviewadapter.BaseViewHolder;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.musicmanager.Music;

import java.util.List;

/**
 * 创建时间:2020/9/3
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class TopListAdapter extends BaseAdapter<TopListDetailEntity.ListBean, BaseViewHolder> {

    public TopListAdapter() {
        super(R.layout.itemiv_top_list, null);
    }

    public TopListAdapter(@Nullable List<TopListDetailEntity.ListBean> data) {
        super(R.layout.itemiv_top_list, data);
    }

    //使得GridLayoutManager中的HeaderView和FooterView可以独占一行
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mData != null && mData.get(position).getTracks().size() > 0) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, TopListDetailEntity.ListBean data) {
        //是否显示底部的MarginView
        holder.getView(R.id.margin_barsize).setVisibility(isShowDivider(holder) ? View.VISIBLE : View.GONE);

        if (data.getTracks() != null && data.getTracks().size() == 3) {
            holder.getView(R.id.ll_tv).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_title).setVisibility(View.GONE);
            TopListDetailEntity.ListBean.TracksBean bean1 = data.getTracks().get(0);
            TopListDetailEntity.ListBean.TracksBean bean2 = data.getTracks().get(1);
            TopListDetailEntity.ListBean.TracksBean bean3 = data.getTracks().get(2);
            holder.setText(R.id.tv_seq1, "1. " + bean1.getFirst() + "- " + bean1.getSecond());
            holder.setText(R.id.tv_seq2, "2. " + bean2.getFirst() + "- " + bean2.getSecond());
            holder.setText(R.id.tv_seq3, "3. " + bean3.getFirst() + "- " + bean3.getSecond());
        } else {
            holder.getView(R.id.ll_tv).setVisibility(View.GONE);
            holder.getView(R.id.tv_title).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_title, data.getName());
        }
        ImageView sharedElementView = (ImageView) holder.getView(R.id.iv_coverImg);
        Glide.with(holder.itemView)
                .load(data.getCoverImgUrl())
                .into(sharedElementView);
        // 把每个图片视图设置不同的Transition名称, 防止在一个视图内有多个相同的名称, 在变换的时候造成混乱
        ViewCompat.setTransitionName(sharedElementView, data.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(sharedElementView, sharedElementView.getTransitionName())
                        .build();
                Log.d(TAG, "----onItemClick: " + sharedElementView.getTransitionName());
                Bundle bundle = new Bundle();
                bundle.putLong(PlayListDetailFragment.KEY_ID, data.getId());
                bundle.putString(PlayListDetailFragment.KEY_NAME, data.getName());
                bundle.putString(PlayListDetailFragment.KEY_COVER, data.getCoverImgUrl());
                Navigation.findNavController(view)
                        .navigate(R.id.playListDetailFragment, bundle, null, extras);
            }
        });
    }

    private static final String TAG = "TopListAdapter";

    private boolean isShowDivider(BaseViewHolder holder) {
        return getRealPosition(holder) == getDatas().size() - 1;
    }
}
