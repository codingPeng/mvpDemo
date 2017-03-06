package com.wp.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.wp.mvp.bean.GirlsList;
import com.wp.mvp.presenter.IGirlPresenterimpl;
import com.wp.mvp.ui.adapter.BaseRecycleViewAdapter;
import com.wp.mvp.ui.adapter.GirlsAdapter;
import com.wp.mvp.ui.adapter.LoadMoreListener;
import com.wp.mvp.view.IGirlView;
import com.wp.tnewsapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements IGirlView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_home_recycleView)
    RecyclerView recycleView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private GirlsAdapter girlsAdapter ;
    private IGirlPresenterimpl girlPresenterimpl;
    private List<GirlsList.ResultsBean> Beans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // 初始化toolbar
        initToolbar();
        // 初始化RecycleView
        initRecycleView();

        // 初始化数据
        initData();
    }

    private void initToolbar() {
        toolbar.setTitle(getResources().getString(R.string.app_name));

    }

    private void initRecycleView() {
        // 设置item的高度不变
       recycleView.setHasFixedSize(true);
        // 初始化linlayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // 设置LayoutManger
       recycleView.setLayoutManager(linearLayoutManager);
        // 初始化adapter
       girlsAdapter = new GirlsAdapter();
        // 设置adapter
        recycleView.setAdapter(girlsAdapter);

        girlsAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.ROnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseRecycleViewAdapter.BaseViewHolder viewHolder) {
                HomeDetailsActivity.launch(HomeActivity.this,
                        Beans.get(position).getDesc(), Beans.get(position).getUrl());
            }
        });

        // 滑动到底部加载更多
        recycleView.addOnScrollListener(new LoadMoreListener(linearLayoutManager) {
            @Override
            public void onLoadingMore(int currentPage) {
                girlPresenterimpl.loadMore();
            }
        });


    }

    private void initData() {
        // 实例化P层实现类
        girlPresenterimpl = new IGirlPresenterimpl(this);
        girlPresenterimpl.loadData();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    // p层加载完数据对UI进行填充
    @Override
    public void fillData(List<GirlsList.ResultsBean> resultsBeen) {
        Beans.addAll(resultsBeen);
        girlsAdapter.setDataSources(Beans);
        girlsAdapter.notifyDataSetChanged();
    }

    /**
     * 加载更多
     * @param page
     * @param count
     * @param resultsBeen
     */
    @Override
    public void loadMore(int page, int count, List<GirlsList.ResultsBean> resultsBeen) {
        Beans.addAll(resultsBeen);
        // 判断是否还有更多的数据 进行局部更新
        if (page * count - count - 1 > 0) {
            girlsAdapter.notifyItemRangeChanged(page * count -count -1, count);
        }
        else {
            girlsAdapter.notifyDataSetChanged();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
