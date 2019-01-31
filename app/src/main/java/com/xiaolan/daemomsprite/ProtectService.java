package com.xiaolan.daemomsprite;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class ProtectService extends Service {
    private MyBinder mMyBinder;
    private MyConn mMyConn;

    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("ProtectService", "onCreate");
        mMyBinder = new MyBinder();
        if (mMyConn == null) {
            mMyConn = new MyConn();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ProtectService", "onStartCommand : " + flags);
        bindService(new Intent(ProtectService.this, MainService.class), mMyConn, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    public class MyBinder extends ICat.Stub {
        @Override
        public void getServiceName() throws RemoteException {

        }
    }

    class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("ProtectService", "onServiceConnected : " + componentName);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("ProtectService", "onServiceDisconnected : " + componentName);
            startService(new Intent(ProtectService.this, MainService.class));
            bindService(new Intent(ProtectService.this, MainService.class), mMyConn, Context.BIND_IMPORTANT);
        }
    }
}
