package com.lianer.ethwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lianer.ethwallet.stuff.HLError;
import com.lianer.ethwallet.stuff.HLSubscriber;

import org.web3j.crypto.WalletFile;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createWallet() {
        Flowable<HLWallet> walletFileFlowable = InitWalletManager.shared().generateWallet(this, "111111", "111111");
        walletFileFlowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HLSubscriber<HLWallet>() {
                    @Override
                    protected void success(HLWallet data) {
                        Log.i("address", data.getAddress());
                    }

                    @Override
                    protected void failure(HLError error) {
                        Log.i("address", error.getMessage());
                    }
                });
    }
}
