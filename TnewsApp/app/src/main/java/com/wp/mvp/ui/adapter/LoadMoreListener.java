package com.wp.mvp.ui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public abstract class LoadMoreListener extends RecyclerView.OnScrollListener{

    private int primaryTotal = 0;
    private int lastScrollPosition, totalItemCount, visibleItemCount;
    private boolean loading = true;
    private int currentPage = 1;
    private LinearLayoutManager mLinearLayoutManager;

    public LoadMoreListener(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager = linearLayoutManager;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        lastScrollPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

        if (loading) {
            if (totalItemCount > primaryTotal) {
                loading = false;
                primaryTotal = totalItemCount;
            }
        }
        if (!loading && (visibleItemCount > 0)
                    && (lastScrollPosition >= totalItemCount - 1)) {
            currentPage++;
            onLoadingMore(currentPage);
            loading = true;
        }
    }
    public abstract void onLoadingMore(int currentPage);
}
