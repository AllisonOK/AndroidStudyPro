package com.example.dispatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.text).setOnClickListener(this);
        findViewById(R.id.text).setOnTouchListener(this);

        StringBuffer a = new StringBuffer("A");
        StringBuffer b = new StringBuffer("B");
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("dispatch" , "MainActivity dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
//        return false;
//        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("dispatch" , "MainActivity onTouchEvent");
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("dispatch" , "MyTextView onTouch");
        return false;
    }

    @Override
    public void onClick(View v) {
        Log.i("dispatch" , "MyTextView onClick");
    }
}
