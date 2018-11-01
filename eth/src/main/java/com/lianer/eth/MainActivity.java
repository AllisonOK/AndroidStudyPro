package com.lianer.eth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import static org.web3j.tx.ManagedTransaction.GAS_PRICE;
import static org.web3j.tx.Transfer.GAS_LIMIT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            String transactionHash = null;
            try {
                transactionHash = TransactionClient.getInstance().sendTransaction2("0x65C18AE8Cc5DecfD092DEF023e951087d1cF4Dbf", "0x549530526d0265f0334e97cc5306d81f4aec011f",
                        GAS_PRICE, GAS_LIMIT, "0.5", "e27dba8477344986a87b035c6763ae3e48e77225da0c1f20879b98047a306934");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //获得到transactionHash后就可以到以太坊的网站上查询这笔交易的状态了
            Log.i("transactionHash", transactionHash);
        }).start();
    }
}
