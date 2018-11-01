package com.lianer.ethwallet.wallet;


import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianer.ethwallet.common.ACache;
import com.lianer.ethwallet.common.Constant;
import com.lianer.ethwallet.common.Singleton;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class HLWalletManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HashMap<String, HLWallet> mHLWalletHashMap = new LinkedHashMap<>();
    private HLWallet mCurrentWallet;

    public void saveWallet(Context context, HLWallet wallet) {
        updateWallet(context, wallet);
        switchCurrentWallet(context, wallet);
    }

    public void updateWallet(Context context, HLWallet wallet) {
        mHLWalletHashMap.put(wallet.getAddress(), wallet);
        updateWallets(context);
    }

    public void updateWallets(Context context) {
        WalletStore walletStore = new WalletStore();
        walletStore.wallets = mHLWalletHashMap;
        String json = Singleton.gson().toJson(walletStore, WalletStore.class);
        ACache.get(context).put(Constant.WALLETS, json);
    }

    public void switchCurrentWallet(Context context, @NonNull HLWallet item) {
        if (mCurrentWallet != null) {
            mCurrentWallet.isCurrent = false;
        }
        mCurrentWallet = item;
        item.isCurrent = true;
        ACache.get(context).put(Constant.CURRENT_ADDRESS, item.getAddress());
        EventBus.getDefault().post(new SwitchWalletEvent());
    }

    public HLWallet getCurrentWallet(Context context) {
        if (mCurrentWallet == null) {
            //if not current wallet, try to load wallet files
            loadWallets(context);
            //no wallets, return null
            if (mHLWalletHashMap.isEmpty()) {
                return null;
            } else {
                //if get wallets, try to get user prefer wallet
                String address = ACache.get(context).getAsString(Constant.CURRENT_ADDRESS);
                if (address.length() > 0) {
                    //get user prefer wallet
                    switchCurrentWallet(context, mHLWalletHashMap.get(address));
                } else {
                    // if not prefer, get the first wallet
                    Iterator<String> iterator = mHLWalletHashMap.keySet().iterator();
                    switchCurrentWallet(context, mHLWalletHashMap.get(iterator.next()));

                }
            }
        }
        return mCurrentWallet;
    }

    public List<HLWallet> getWallets() {
        return new ArrayList<>(mHLWalletHashMap.values());
    }

    public void loadWallets(Context context) {
        try {
            String jsonString = ACache.get(context).getAsString(Constant.WALLETS);
            if (jsonString != null) {
                WalletStore store = objectMapper.readValue(jsonString, WalletStore.class);
                if (store != null) {
                    mHLWalletHashMap.clear();
                    mHLWalletHashMap.putAll(store.wallets);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isWalletExist(String address) {
        Set<String> strings = mHLWalletHashMap.keySet();
        for (String s : strings) {
            if (s.equalsIgnoreCase(address)){
                return true;
            }
        }
        return false;
    }

    public void deleteWallet (Context context) {
        mCurrentWallet = null;
        mHLWalletHashMap.clear();
        ACache.get(context).clear();
    }

    // ---------------- singleton stuff --------------------------
    public static HLWalletManager shared() {
        return Holder.singleton;
    }

    private HLWalletManager() {

    }

    private static class Holder {

        private static final HLWalletManager singleton = new HLWalletManager();

    }
}
