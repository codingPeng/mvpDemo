package com.wp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wp.MyApplication;

/**
 * Created by Administrator on 2017/3/4 0004.
 * 判断网络连接类
 */

public class NetWorkUtils {

    public static boolean isNetWorkConnected() {

        if (MyApplication.getApplication() != null) {
            ConnectivityManager manager = (ConnectivityManager) MyApplication.
                    getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return  networkInfo.isAvailable();
            }
        }
        return  false;
    }

}
