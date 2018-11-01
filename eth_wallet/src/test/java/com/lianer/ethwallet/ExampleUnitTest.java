package com.lianer.ethwallet;

import com.lianer.ethwallet.model.WalletModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testImportKeystore() {
        String password = "asdfasf";
        String keystore = "{\"address\":\"355e3d899808977fcb6880636c5de263f4a5b977\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"59b329547773db9401c7edc4c4a60f43\"},\"ciphertext\":\"f27dd6dee9cb34d282352592acb38039dca0b8e39ee923b7737c7482520405a9\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"712d8b1785efa7e8d345e02ad96c60b8b572278762ff5527c148567331c50941\"},\"mac\":\"959d6e17f81e73a98b13891722f66a525c1be1165cfc10c42f9e65a0853171de\"},\"id\":\"cb658c67-25bc-4953-976a-8c08034feb53\",\"version\":3}";


        MainActivity mainActivity = new MainActivity();
        WalletModel walletModel = new WalletModel(mainActivity);
//        walletModel.importWalletByKeystore(password, keystore);
    }
}