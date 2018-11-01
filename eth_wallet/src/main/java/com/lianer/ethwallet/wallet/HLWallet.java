package com.lianer.ethwallet.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lianer.ethwallet.common.Constant;

import org.web3j.crypto.WalletFile;

public class HLWallet {

    public WalletFile walletFile;

    @JsonIgnore
    public boolean isCurrent = false;

    public HLWallet() {

    }

    public HLWallet(WalletFile walletFile) {
        this.walletFile = walletFile;
    }

    public String getAddress(){
        return Constant.PREFIX_16 + this.walletFile.getAddress();
    }

    public WalletFile getWalletFile() {
        return walletFile;
    }

    public void setWalletFile(WalletFile walletFile) {
        this.walletFile = walletFile;
    }
}
