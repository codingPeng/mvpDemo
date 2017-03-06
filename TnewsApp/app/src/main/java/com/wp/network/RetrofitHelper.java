package com.wp.network;

import android.os.Environment;

import com.wp.MyApplication;
import com.wp.network.api.ApiService;
import com.wp.utils.NetWorkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/4 0004.
 * retrofit的辅助类
 */

public class RetrofitHelper {

    // 创建okhttpClient对象
    private static OkHttpClient okHttpClient = null;

    // 声明 baseUrl
    private static final String baseUrl = "http://gank.io/api/";

    static {
        initOkhttpClient();
    }

    public static ApiService createApiService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 运用了动态代理模式 返回ApiService对象
        return retrofit.create(ApiService.class);
    }

    /*
        *初始化Okhttpclient
     */
    private static void initOkhttpClient() {
        // okhttpLog拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (okHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (okHttpClient == null ){
                    // 设置 缓存
                    Cache cache = new Cache(new File(
                            MyApplication.getApplication().getCacheDir(), "httpCache"),
                            1024 * 1024 * 1000
                    );

                    okHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addNetworkInterceptor(new CacheInterceptor())
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    /*
    *  okhttp 缓存拦截器
    *
    * */
    private static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            // 有网络时，超时1小时
            int maxAge = 60 * 60;

            // 无网时， 超时1天
            int maxStale = 60 * 60 * 24;
            Request request = chain.request();
            if (NetWorkUtils.isNetWorkConnected()) {
                // 有网时从网络获取
                request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            }
            else {
                // 无网时从本地缓存获取
                request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (NetWorkUtils.isNetWorkConnected()) {
                response.newBuilder()
                        //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            }
            else {
                response.newBuilder()
                        //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();

            }
            return response;
        }
    }


}
