package com.fr1014.frecyclerviewadapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建时间:2020/9/3
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    //Views indexed with their IDs
    private final SparseArray<View> views;
    private BaseAdapter adapter;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
    }

    public BaseViewHolder setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public BaseViewHolder setTextSize(@IdRes int viewId, float size) {
        TextView view = getView(viewId);
        view.setTextSize(size);
        return this;
    }


//    public BaseViewHolder setImage

    public BaseViewHolder setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public BaseViewHolder addOnClickListener(@IdRes final int... viewIds) {
        for (int viewId : viewIds) {
            final View view = getView(viewId);
            if (view != null) {
                if (!view.isClickable()) {
                    view.setClickable(true);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.getOnItemClickListener() != null) {
                            int potion = getAdapterPosition();
                            if (potion == RecyclerView.NO_POSITION) {
                                return;
                            }
                            int count = adapter.getHeaderView() == null ? 0 : 1;
                            potion -= count;
                            adapter.getOnItemClickListener().onItemClick(adapter, v, potion);
                        }

                    }
                });
            }
        }
        return this;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

}
