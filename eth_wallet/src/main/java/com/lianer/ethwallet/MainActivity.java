package com.lianer.ethwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.lianer.ethwallet.model.WalletModel;
import com.lianer.ethwallet.stuff.HLError;
import com.lianer.ethwallet.stuff.HLSubscriber;
import com.lianer.ethwallet.wallet.HLWallet;
import com.lianer.ethwallet.wallet.InitWalletManager;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    String password = "asdfasf";
    String keystore = "{\"address\":\"355e3d899808977fcb6880636c5de263f4a5b977\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"59b329547773db9401c7edc4c4a60f43\"},\"ciphertext\":\"f27dd6dee9cb34d282352592acb38039dca0b8e39ee923b7737c7482520405a9\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"712d8b1785efa7e8d345e02ad96c60b8b572278762ff5527c148567331c50941\"},\"mac\":\"959d6e17f81e73a98b13891722f66a525c1be1165cfc10c42f9e65a0853171de\"},\"id\":\"cb658c67-25bc-4953-976a-8c08034feb53\",\"version\":3}";
    String mnemonic = "";
    String privateKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WalletModel walletModel = new WalletModel(this);

        //创建钱包
        //walletModel.createWallet(password);

        //keystore导入
        walletModel.importWalletByKeystore(password, keystore);

        //助记词导入
        walletModel.importWalletByMnmonic(password, mnemonic);

        //私钥导入
        walletModel.importWalletByPrivateKey(password, privateKey);
    }

}
