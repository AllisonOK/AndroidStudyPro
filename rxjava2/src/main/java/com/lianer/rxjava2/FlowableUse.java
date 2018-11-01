package com.lianer.rxjava2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FlowableUse extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demo2();
    }

    public void demo2() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Flowable.just(list)
                .map(new Function<List<Integer>, Object>() {
                    @Override
                    public Object apply(List<Integer> integers) throws Exception {
                        return integers;
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) {
                        Log.i("flowableTest", "flowableTest ==== " + o.toString());
                    }
                });
    }
}
