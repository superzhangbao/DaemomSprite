package com.xiaolan.daemomsprite;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public Context getContext() {
        return mContext;
    }
}
