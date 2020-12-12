package com.fr1014.mycoludmusic.ui.home.toplist;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fr1014.frecyclerviewadapter.BaseAdapter;
import com.fr1014.frecyclerviewadapter.BaseViewHolder;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;

import java.util.List;

/**
 * 创建时间:2020/9/3
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class TopListAdapter extends BaseAdapter<TopListDetailEntity.ListBean, BaseViewHolder> implements BaseAdapter.OnItemClickListener {

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
        Glide.with(holder.itemView)
                .load(data.getCoverImgUrl())
                .into((ImageView) holder.getView(R.id.iv_coverImg));
    }

    @Override
    public void onItemClick(BaseAdapter adapter, View view, int position) {
        Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putLong(PlayListDetailFragment.KEY_ID, mData.get(position).getId());
        Navigation.findNavController(view).navigate(R.id.playListDetailFragment, bundle);
    }

    private boolean isShowDivider(BaseViewHolder holder) {
        return getRealPosition(holder) == getDatas().size() - 1;
    }
}
