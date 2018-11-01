package com.lianer.ethwallet;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianer.ethwallet.stuff.HLError;
import com.lianer.ethwallet.stuff.LWallet;
import com.lianer.ethwallet.stuff.ReplyCode;

import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.security.SecureRandom;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip32.networks.Bitcoin;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.SeedCalculator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.BIP44;
import io.reactivex.Flowable;

/**
 * <pre>
 * Create by  :    L
 * Create Time:    2018/6/19
 * Brief Desc :
 * </pre>
 */
public class InitWalletManager {


    /**
     * generate a random group of mnemonics
     * 生成一组随机的助记词
     */
    public String generateMnemonics() {
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(entropy, sb::append);
        return sb.toString();
    }

    /**
     * @param context   app context 上下文
     * @param password  the wallet password(not the bip39 password) 钱包密码(而不是BIP39的密码)
     * @param mnemonics 助记词
     * @return wallet 钱包
     */
    public Flowable<HLWallet> generateWallet(Context context,
                                             String password,
                                             String mnemonics) {
        Flowable<String> flowable = Flowable.just(mnemonics);

        return flowable
                .map(s -> {
                    ECKeyPair keyPair = generateKeyPair(s);
                    WalletFile walletFile = Wallet.createLight(password, keyPair);
                    HLWallet hlWallet = new HLWallet(walletFile);
                    HLWalletManager.shared().saveWallet(context, hlWallet);

                    String keystore = Singleton.gson().toJson(walletFile);
//                    Log.i("InitWalletManager","keystore:\n" + keystore);
                    return hlWallet;
                });
    }

    public Flowable<HLWallet> importMnemonic(Context context,
                                             String password,
                                             String mnemonics) {
        Flowable<String> flowable = Flowable.just(mnemonics);

        return flowable
                .flatMap(s -> {
                    ECKeyPair keyPair = generateKeyPair(s);
                    WalletFile walletFile = Wallet.createLight(password, keyPair);
                    HLWallet hlWallet = new HLWallet(walletFile);
                    if (HLWalletManager.shared().isWalletExist(hlWallet.getAddress())) {
                        return Flowable.error(new HLError(ReplyCode.walletExisted, new Throwable("Wallet existed!")));
                    }
                    HLWalletManager.shared().saveWallet(context, hlWallet);
                    return Flowable.just(hlWallet);
                });
    }

    /**
     * generate key pair to create eth wallet
     * 生成KeyPair , 用于创建钱包
     */
    public ECKeyPair generateKeyPair(String mnemonics) {
        // 1. we just need eth wallet for now
        AddressIndex addressIndex = BIP44
                .m()
                .purpose44()
                .coinType(60)
                .account(0)
                .external()
                .address(0);
        // 2. calculate seed from mnemonics , then get master/root key ; Note that the bip39 passphrase we set "" for common
        ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(new SeedCalculator().calculateSeed(mnemonics, ""), Bitcoin.MAIN_NET);
        Log.i("InitWalletManager","mnemonics:" + mnemonics);
        String extendedBase58 = rootKey.extendedBase58();
        Log.i("InitWalletManager","extendedBase58:" + extendedBase58);

        // 3. get child private key deriving from master/root key
        ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
        String childExtendedBase58 = childPrivateKey.extendedBase58();
        Log.i("InitWalletManager","childExtendedBase58:" + childExtendedBase58);

        // 4. get key pair
        byte[] privateKeyBytes = childPrivateKey.getKey();
        ECKeyPair keyPair = ECKeyPair.create(privateKeyBytes);

        // we 've gotten what we need
        String privateKey = childPrivateKey.getPrivateKey();
        String publicKey = childPrivateKey.neuter().getPublicKey();
        String address = Keys.getAddress(keyPair);

        Log.i("InitWalletManager","privateKey:" + privateKey);
        Log.i("InitWalletManager","publicKey:" + publicKey);
        Log.i("InitWalletManager","address:" + Constant.PREFIX_16 + address);

        return keyPair;
    }

    public Flowable<HLWallet> importPrivateKey(Context context, String privateKey, String password) {
        if (privateKey.startsWith(Constant.PREFIX_16)) {
            privateKey = privateKey.substring(Constant.PREFIX_16.length());
        }
        Flowable<String> flowable = Flowable.just(privateKey);
        return flowable.flatMap(s -> {
            byte[] privateBytes = Hex.decode(s);
            ECKeyPair ecKeyPair = ECKeyPair.create(privateBytes);
            WalletFile walletFile = Wallet.createLight(password, ecKeyPair);
            HLWallet hlWallet = new HLWallet(walletFile);
            if (HLWalletManager.shared().isWalletExist(hlWallet.getAddress())) {
                return Flowable.error(new HLError(ReplyCode.walletExisted, new Throwable("Wallet existed!")));
            }
            HLWalletManager.shared().saveWallet(context, hlWallet);
            return Flowable.just(hlWallet);
        });
    }

    /**
     * web3j的导入Keystore方式,容易OOM
     */
    @Deprecated
    public Flowable<HLWallet> importKeystoreViaWeb3j(Context context, String keystore, String password) {
        return Flowable.just(keystore)
                .flatMap(s -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
                    ECKeyPair keyPair = Wallet.decrypt(password, walletFile);
                    HLWallet hlWallet = new HLWallet(walletFile);

                    WalletFile generateWalletFile = Wallet.createLight(password, keyPair);
                    if (!generateWalletFile.getAddress().equalsIgnoreCase(walletFile.getAddress())) {
                        return Flowable.error(new HLError(ReplyCode.failure, new Throwable("address doesn't match private key")));
                    }

                    if (HLWalletManager.shared().isWalletExist(hlWallet.getAddress())) {
                        return Flowable.error(new HLError(ReplyCode.walletExisted, new Throwable("Wallet existed!")));
                    }
                    HLWalletManager.shared().saveWallet(context, hlWallet);
                    return Flowable.just(hlWallet);
                });
    }

    public Flowable<HLWallet> importKeystore(Context context, String keystore, String password) {
        return Flowable.just(keystore)
                .flatMap(s -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
                    ECKeyPair keyPair = LWallet.decrypt(password, walletFile);
                    HLWallet hlWallet = new HLWallet(walletFile);

                    WalletFile generateWalletFile = Wallet.createLight(password, keyPair);
                    if (!generateWalletFile.getAddress().equalsIgnoreCase(walletFile.getAddress())) {
                        return Flowable.error(new HLError(ReplyCode.failure, new Throwable("address doesn't match private key")));
                    }

                    if (HLWalletManager.shared().isWalletExist(hlWallet.getAddress())) {
                        return Flowable.error(new HLError(ReplyCode.walletExisted, new Throwable("Wallet existed!")));
                    }
                    HLWalletManager.shared().saveWallet(context, hlWallet);
                    return Flowable.just(hlWallet);
                });
    }


    // ---------------- singleton stuff --------------------------
    public static InitWalletManager shared() {
        return InitWalletManager.Holder.singleton;
    }

    private InitWalletManager() {

    }

    private static class Holder {

        private static final InitWalletManager singleton = new InitWalletManager();

    }
}
