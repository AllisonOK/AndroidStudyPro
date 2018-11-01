package com.lianer.networklisten;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.lianer.networklisten.net.NetChangeObserver;
import com.lianer.networklisten.net.NetUtils;

public class MainActivity extends BaseActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
    }

    @Override
    protected void onNetworkDisConnected() {
        textView.setText("网络断开");
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        textView.setText("网络正常");
    }

}
