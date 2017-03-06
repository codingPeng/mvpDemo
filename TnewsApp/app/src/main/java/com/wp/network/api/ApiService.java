package com.wp.network.api;

import com.wp.mvp.bean.GirlsList;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/3/4 0004.
 */

public interface ApiService {

    @GET("data/福利/{count}/{page}")
    Flowable<GirlsList> getGrilsData(
            @Path("page") int page, @Path("count") int count);


}
