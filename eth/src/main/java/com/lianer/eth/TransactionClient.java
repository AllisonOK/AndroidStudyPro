package com.lianer.eth;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class TransactionClient {
	private static Web3j web3j;
	private static Admin admin;
	private static TransactionClient transactionClient;

	public static TransactionClient getInstance() {
		if (transactionClient == null) {
			transactionClient = new TransactionClient();
			web3j = Web3jFactory.build(new HttpService("https://ropsten.infura.io/v3/08c2aed209af4ff3a41ac200c579a73b"));
			admin = AdminFactory.build(new HttpService("https://ropsten.infura.io/v3/08c2aed209af4ff3a41ac200c579a73b"));
		}
		return transactionClient;
	}

	/**
	 * 获取余额
	 *
	 * @param address 钱包地址
	 * @return 余额
	 */
	public BigInteger getBalance(String address) {
		BigInteger balance = null;
		try {
			EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
			balance = ethGetBalance.getBalance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("address " + address + " balance " + balance + "wei");
		return balance;
	}

	/**
	 * 生成一个普通交易对象
	 *
	 * @param fromAddress 放款方
	 * @param toAddress   收款方
	 * @param nonce       交易序号
	 * @param gasPrice    gas 价格
	 * @param gasLimit    gas 数量
	 * @param value       金额
	 * @return 交易对象
	 */
	private static Transaction makeTransaction(String fromAddress, String toAddress,
											   BigInteger nonce, BigInteger gasPrice,
											   BigInteger gasLimit, BigInteger value) {
		Transaction transaction;
		transaction = Transaction.createEtherTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, value);
		return transaction;
	}

	/**
	 * 获取普通交易的gas上限
	 *
	 * @param transaction 交易对象
	 * @return gas 上限
	 */
	private static BigInteger getTransactionGasLimit(Transaction transaction) {
		BigInteger gasLimit = BigInteger.ZERO;
		try {
			EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
			gasLimit = ethEstimateGas.getAmountUsed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gasLimit;
	}

	/**
	 * 获取账号交易次数 nonce
	 *
	 * @param address 钱包地址
	 * @return nonce
	 */
	public static BigInteger getTransactionNonce(String address) {
		BigInteger nonce = BigInteger.ZERO;
		try {
			EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
			nonce = ethGetTransactionCount.getTransactionCount();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nonce;
	}

//	/**
//	 * 发送一个普通交易
//	 *
//	 * @return 交易 Hash
//	 */
//	public static String sendTransaction() {
//		String password = "11111111";
//		BigInteger unlockDuration = BigInteger.valueOf(60L);
//		BigDecimal amount = new BigDecimal("0.01");
//		String txHash = null;
//		try {
//			BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
//			Transaction transaction = makeTransaction(fromAddress, toAddress,
//					null, null, null, value);
//			//不是必须的 可以使用默认值
//			BigInteger gasLimit = getTransactionGasLimit(transaction);
//			//不是必须的 缺省值就是正确的值
//			BigInteger nonce = getTransactionNonce(fromAddress);
//			//该值为大部分矿工可接受的gasPrice
//			BigInteger gasPrice = Convert.toWei(defaultGasPrice, Convert.Unit.GWEI).toBigInteger();
//			transaction = makeTransaction(fromAddress, toAddress,
//					nonce, gasPrice,
//					gasLimit, value);
//			EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
//			txHash = ethSendTransaction.getTransactionHash();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("tx hash " + txHash);
//		return txHash;
//	}

	/**
	 * 发起交易
	 * @param fromAddress    放款方    0x65C18AE8Cc5DecfD092DEF023e951087d1cF4Dbf
	 * @param toAddress		 收款方    0x0532b851bF3b89D66A91d61af3E454fEeb7e4B56
	 * @param gasPrice		 		  GAS_PRICE
	 * @param gasLimit				  GAS_LIMIT
	 * @param number		 金额	  0.5
	 * @param privateKey    转账人私钥 e27dba8477344986a87b035c6763ae3e48e77225da0c1f20879b98047a306934
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public String sendTransaction2(String fromAddress, String toAddress, BigInteger gasPrice,
										BigInteger gasLimit, String number, String privateKey) throws ExecutionException, InterruptedException {
		//获取交易序号
		EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
					fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
		BigInteger nonce = ethGetTransactionCount.getTransactionCount();

		//创建交易
		BigInteger value = Convert.toWei(number, Convert.Unit.ETHER).toBigInteger();
		RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
				nonce, gasPrice, gasLimit, toAddress, value);

		Credentials credentials = Credentials.create(privateKey);

		//签名Transaction，这里要对交易做签名
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		String hexValue = Numeric.toHexString(signedMessage);

		//发送交易
		EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
		//交易完成
		return ethSendTransaction.getTransactionHash();
	}

}
