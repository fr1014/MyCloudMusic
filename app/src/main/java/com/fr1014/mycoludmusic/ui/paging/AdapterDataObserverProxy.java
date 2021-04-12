package com.fr1014.mycoludmusic.ui.paging;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by fanrui on 2021/4/12
 * Describe: 解决 Android RecyclerView + Paging Library 添加头部刷新会自动滚动的问题
 *
 * 参考：https://blog.csdn.net/cekiasoo/article/details/81990475
 *      https://juejin.cn/post/6844903814189826062
 */
public class AdapterDataObserverProxy extends RecyclerView.AdapterDataObserver {
    RecyclerView.AdapterDataObserver adapterDataObserver;
    int headerCount;

    public AdapterDataObserverProxy(RecyclerView.AdapterDataObserver adapterDataObserver, int headerCount) {
        this.adapterDataObserver = adapterDataObserver;
        this.headerCount = headerCount;
    }
    @Override
    public void onChanged() {
        adapterDataObserver.onChanged();
    }
    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        adapterDataObserver.onItemRangeChanged(positionStart + headerCount, itemCount);
    }
    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        adapterDataObserver.onItemRangeChanged(positionStart + headerCount, itemCount, payload);
    }

    // 当第n个数据被获取，更新第n+1个position
    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        adapterDataObserver.onItemRangeInserted(positionStart + headerCount, itemCount);
    }
    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        adapterDataObserver.onItemRangeRemoved(positionStart + headerCount, itemCount);
    }
    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition + headerCount, toPosition + headerCount, itemCount);
    }
}