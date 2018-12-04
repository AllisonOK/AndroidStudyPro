package com.lianer.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        NotificationUtils notification = new NotificationUtils(this);
        notification.sendNotification("",
                "", "", "", MainActivity.class);
    }

}
