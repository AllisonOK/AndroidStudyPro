package com.lianer.nestsource;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //字体大小
        float value1 = px2sp(48f);
        float value2 = px2sp(36f);
        float value3 = px2sp(30f);
        float value4 = px2sp(28f);
        float value5 = px2sp(24f);
        float value6 = px2sp(22f);

        float value7 = px2dp(32f);
        float value8 = px2dp(45f);
    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public int px2dp(float pxValue) {
        return (int) (pxValue / getResources().getDisplayMetrics().density + 0.5f);
    }

    public int px2sp(float pxValue) {
        return (int) (pxValue / getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public float px2sp(int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size, getResources().getDisplayMetrics());
    }

    public int px2dip(float pxValue) {
        return (int) (pxValue / getResources().getDisplayMetrics().density + 0.5f);
    }

    public int dip2px(float dipValue) {
        return (int) (dipValue * getResources().getDisplayMetrics().density + 0.5f);
    }

    public int sp2px(float spValue) {
        return (int) (spValue * getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public float sp2px(int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics());
    }
}
