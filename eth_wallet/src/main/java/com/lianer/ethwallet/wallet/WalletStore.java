package com.lianer.ethwallet.wallet;

import com.lianer.ethwallet.wallet.HLWallet;

import java.util.HashMap;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/5/17
 * Brief Desc :
 * </pre>
 */
public class WalletStore {

    public HashMap<String, HLWallet> wallets = new HashMap<>();

    public WalletStore() {
    }

}
