package com.wp;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/3/4 0004.
 */

public class MyApplication extends Application{

    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static MyApplication getApplication() {
        return application;
    }
}
