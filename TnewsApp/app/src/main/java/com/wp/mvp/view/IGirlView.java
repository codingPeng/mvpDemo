package com.wp.mvp.view;

import com.wp.mvp.bean.GirlsList;

import java.util.List;

/**
 * Created by Administrator on 2017/3/4 0004.
 * v层接口， 定义Activity跟Ui交互的方法
 */

public interface IGirlView {

    // 显示loading进度条
    void showProgress();

    // 去除loading进度条
    void hideProgress();

    // 填充数据
    void fillData(List<GirlsList.ResultsBean> resultsBeen);

    // 下拉加载更多数据
    void loadMore(int page, int count, List<GirlsList.ResultsBean> resultsBeen);



}
