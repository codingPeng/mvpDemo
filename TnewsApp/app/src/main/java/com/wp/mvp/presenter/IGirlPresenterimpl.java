package com.wp.mvp.presenter;

import android.util.Log;

import com.wp.mvp.bean.GirlsList;
import com.wp.mvp.view.IGirlView;
import com.wp.network.RetrofitHelper;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Administrator on 2017/3/4 0004.
 * p层IGrilPresenter 的实现类
 * 并且引入数据，完成后通知view的UI更新
 */

public class IGirlPresenterimpl implements IGirlPresenter{

    private static final String TAG = "IGirlPresenterimpl";

    // 定义数据的页码
    int page = 1;

    // 定义数据的加载数量
    int count = 10;

    // View层接口
    private IGirlView iGirlView;


    // 通过构造函数传入view层
    public IGirlPresenterimpl (IGirlView iGirlView) {
        this.iGirlView = iGirlView;
    }

    @Override
    /**
     * 给View层加载数据
     */
    public void loadData() {
        RetrofitHelper.createApiService()
                .getGrilsData(page,count)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        iGirlView.showProgress();
                    }
                })
                .delay(1000, TimeUnit.MILLISECONDS)
                .filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<GirlsList>() {
                    @Override
                    public boolean test(GirlsList girlsList) {
                        return !girlsList.isError();
                    }
                })
                .map(new Function<GirlsList, List<GirlsList.ResultsBean>>() {

                    @Override
                    public List<GirlsList.ResultsBean> apply(GirlsList girlsList) throws Exception {
                        return girlsList.getResults();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GirlsList.ResultsBean>>() {

                    @Override
                    public void accept(List<GirlsList.ResultsBean> o) throws Exception {
                        Log.d(TAG, "有数据了。。。。。。。。");
                        iGirlView.fillData(o);
                        iGirlView.hideProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iGirlView.hideProgress();
                    }
                });
    }

    @Override
    /**
     * 加载更多数据
     */
    public void loadMore() {
        page++;
        RetrofitHelper.createApiService()
                .getGrilsData(page,count)
                .filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<GirlsList>() {
                    @Override
                    public boolean test(GirlsList girlsList) {
                        return !girlsList.isError();
                    }
                })
                .map(new Function<GirlsList, Object>() {

                    @Override
                    public Object apply(GirlsList girlsList) throws Exception {
                        return girlsList.getResults();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        List<GirlsList.ResultsBean> result = (List<GirlsList.ResultsBean>) o;
                        iGirlView.loadMore(page,count,result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "throwable: " + throwable.getMessage());
                    }
                });
    }
}
