package com.lianer.networklisten;

import android.app.Application;
import android.content.IntentFilter;
import android.os.Build;

import com.lianer.networklisten.net.NetStateReceiver;

public class NetworkApp extends Application {
    NetworkApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //动态注册网络变化广播
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            NetStateReceiver netBroadcastReceiver = new NetStateReceiver();
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
        /*开启网络广播监听*/
        NetStateReceiver.registerNetworkStateReceiver(this);
    }

    @Override
    public void onLowMemory() {
        if (instance != null) {
            NetStateReceiver.unRegisterNetworkStateReceiver(instance);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        super.onLowMemory();
    }
}
