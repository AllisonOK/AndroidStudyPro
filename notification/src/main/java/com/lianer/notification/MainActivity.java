package com.lianer.notification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        NotificationUtil.showNotification(this);
    }

    public void onClick2(View v) {
        NotificationUtil.showNotificationProgress(this);
    }

    public void onClick3(View v) {
        NotificationUtil.showFullScreen(this);
    }

    public void onClick4(View v) {

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        boolean isOpened = manager.areNotificationsEnabled();
        Toast.makeText(MainActivity.this, "是否开启通知" + isOpened, Toast.LENGTH_LONG).show();
        if (!isOpened) {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            NotificationUtil.shwoNotify(MainActivity.this);
        }
    }

}
