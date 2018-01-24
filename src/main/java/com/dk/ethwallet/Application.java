package com.dk.ethwallet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;


import org.web3j.utils.Convert;

/**
 * Hello world!
 *
 */
public class Application {
	public static final String WALLET_NAME = "UTC--2018-01-23T18-02-39.558000000Z--dcce15bd1bf25427c6c64911994ca85b753489c3.json";

    public static void main(String[] args) throws Exception {
    	Web3j web3 = Web3j.build(new HttpService("http://127.0.0.1:8545"));  // defaults to http://localhost:8545/
    	Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
    	String clientVersion = web3ClientVersion.getWeb3ClientVersion();
    	
    	System.out.println("clientVersion: " + clientVersion);

        Credentials credentials =
            WalletUtils.loadCredentials(
                    "123",
                    "/home/ayrton/Development/Blockchain/web3j/wallets/wallet1/" + WALLET_NAME
            );
        
        System.out.println("credencials loaded.");
        System.out.println("Pay me: " + credentials.getAddress());

        menu(web3, credentials);
    }
    
    public static void menu(Web3j web3, Credentials credentials) throws Exception {
        System.out.println("1. Check balance");
        System.out.println("2. Check transactions");
        System.out.println("3. Send eth");
        System.out.print("> ");

        int op = new Scanner(System.in).nextInt();

        switch (op) {
	        case 1:
	            BigInteger wei = checkBalance(web3, credentials.getAddress());        
	            BigDecimal c = Convert.fromWei(wei.toString(), Convert.Unit.ETHER);
	            System.out.println("Balance: " + c + " ETH");
	        	break;
	        case 2:
	        	checkTransactions(web3, credentials);
	        	break;
	        case 3:
	        	payToSomeone(web3, credentials);
	        	break;
        }
    }

    public static BigInteger checkBalance(Web3j web3, String address) throws Exception {
    	// get accounts from transactions
		//  List<String> accounts = web3.ethAccounts()
		// 		.send()
		//    	.getAccounts();

    	EthGetBalance ret = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST)
    		.sendAsync()
    		.get();

    	return ret.getBalance();
    }

    public static void checkTransactions(Web3j web3, Credentials cred) {
    }

    public static void payToSomeone(Web3j web3, Credentials cred) throws Exception {
        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
//        log.info("Sending 1 Wei ("
//                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
    	Scanner scan = new Scanner(System.in);

    	System.out.print("Address> ");
    	String address = scan.nextLine();

    	System.out.print("ETH> ");
    	BigDecimal ethAmount = scan.nextBigDecimal();

    	System.out.println("Sending " + ethAmount + " ETH to " + address);

    	TransactionReceipt receipt = Transfer.sendFunds(
                web3, cred,
                address,  // you can put any address here
                ethAmount, Convert.Unit.ETHER)
            .send();

        System.out.println("Transaction sent. Hash: " + receipt.getTransactionHash());
//        log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
//                + transferReceipt.getTransactionHash());
    }
}
