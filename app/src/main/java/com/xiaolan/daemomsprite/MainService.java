package com.xiaolan.daemomsprite;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.xiaolan.daemomsprite.util.DevicesUtil;

public class MainService extends Service {

    private MyBinder mMyBinder;
    private MyConn mMyConn;
    private Handler mHandler;
    private boolean isStart;

    public static ICat mICat;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isStart) return;
            Log.e("MainService", "----------");
            if (DevicesUtil.isBackground(getApplicationContext()) && mSharedPreferences.getBoolean("flag",false)) {
                Log.e("MainService", "==========");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setAction(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
                startService(new Intent(getApplicationContext(), MainService.class));
                startService(new Intent(getApplicationContext(), ProtectService.class));
            } else {
                mHandler.postDelayed(this, 5000);
            }
        }
    };
    private SharedPreferences mSharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMyBinder = new MyBinder();
        if (mMyConn == null) {
            mMyConn = new MyConn();
        }
        isStart = true;
        mSharedPreferences = getSharedPreferences("info", 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        Log.e("MainService", "onStartCommand : " + flags);
        return START_STICKY;
    }

    class MyBinder extends ICat.Stub {

        @Override
        public void getServiceName() throws RemoteException {

        }
    }

    class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainService", "onServiceConnected : " + componentName);
            mICat = ICat.Stub.asInterface(iBinder);
        }


        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainService", "onServiceDisconnected : " + componentName);
            startService(new Intent(MainService.this, ProtectService.class));
            bindService(new Intent(MainService.this, ProtectService.class), mMyConn, Context.BIND_IMPORTANT);
        }
    }

    private void init() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        this.bindService(new Intent(MainService.this, ProtectService.class), mMyConn, Context.BIND_IMPORTANT);
        mHandler.removeCallbacksAndMessages(null);
        if (mSharedPreferences.getBoolean("flag",false)) {
            Log.e("MainService", "-----true-----");
            mHandler.post(mRunnable);
        } else {
            Log.e("MainService", "-----false-----");
        }
    }

    @Override
    public void onDestroy() {
        isStart = false;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }
}
