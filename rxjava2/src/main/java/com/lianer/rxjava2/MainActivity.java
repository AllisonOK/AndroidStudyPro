package com.lianer.rxjava2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lianer.rxjava2.retrofitrxjava.HttpUtils;
import com.lianer.rxjava2.retrofitrxjava.NewsBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpUtils.getNewsData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<NewsBean>() {
                    @Override
                    public void onNext(NewsBean s) {
                        Log.i("MainActivity", s.getLimit() + "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("MainActivity", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("MainActivity", "完成");
                    }
                });
    }
}
