package com.lianer.ethwallet.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lianer.ethwallet.common.Constant;
import com.lianer.ethwallet.common.Singleton;
import com.lianer.ethwallet.stuff.HLError;
import com.lianer.ethwallet.stuff.HLSubscriber;
import com.lianer.ethwallet.wallet.HLWallet;
import com.lianer.ethwallet.wallet.InitWalletManager;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 钱包
 */
public class WalletModel {

    Context mContext;

    public WalletModel(Context context) {
        this.mContext = context;
    }

    public void createWallet(String password) {
        Log.i(Constant.TAG, "钱包密码 = " + password);
        Flowable.just(password)
                .map(s -> {
                    String mnemonic = InitWalletManager.shared().generateMnemonics();
                    Log.i(Constant.TAG, "助记词 = " + mnemonic);
                    return mnemonic;
                })
                .flatMap(s -> InitWalletManager.shared().generateWallet(mContext, password, s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HLSubscriber<HLWallet>() {
                    @Override
                    protected void success(HLWallet data) {
                        Log.i(Constant.TAG, "钱包文件 = " + new Gson().toJson(data.getWalletFile()));
                    }

                    @Override
                    protected void failure(HLError error) {
                        Log.i(Constant.TAG, "错误提示 = " + error.getMessage());
                    }
                });
    }

    public void importWalletByKeystore(String password, String keystore) {
        Log.i(Constant.TAG, "钱包密码 = " + password);
        Log.i(Constant.TAG, "钱包keystore = " + keystore);
        InitWalletManager.shared().importKeystore(mContext, keystore, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HLSubscriber<HLWallet>() {

                    @Override
                    protected void success(HLWallet data) {
                        Log.i(Constant.TAG, "钱包文件 = " + Singleton.gson().toJson(data.getWalletFile()));
                    }

                    @Override
                    protected void failure(HLError error) {
                        Log.i(Constant.TAG, "错误提示 = " + error.getMessage());
                    }
                });
    }

    public void importWalletByMnmonic(String password, String mnemonic) {
        InitWalletManager.shared().importMnemonic(mContext, password, mnemonic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HLSubscriber<HLWallet>() {
                    @Override
                    protected void success(HLWallet data) {
                        Log.i(Constant.TAG, "钱包文件 = " + Singleton.gson().toJson(data.getWalletFile()));
                    }

                    @Override
                    protected void failure(HLError error) {
                        Log.i(Constant.TAG, "错误提示 = " + error.getMessage());
                    }
                });
    }

    public void importWalletByPrivateKey(String password, String privateKey) {
        InitWalletManager.shared().importPrivateKey(mContext, privateKey, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HLSubscriber<HLWallet>() {
                    @Override
                    protected void success(HLWallet data) {
                        Log.i(Constant.TAG, "钱包文件 = " + Singleton.gson().toJson(data.getWalletFile()));
                    }

                    @Override
                    protected void failure(HLError error) {
                        Log.i(Constant.TAG, "错误提示 = " + error.getMessage());
                    }
                });
    }

}
