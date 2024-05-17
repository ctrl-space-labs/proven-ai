package dev.ctrlspace.provenai.blockchain;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.DefaultBlockParameterName;
import java.math.BigInteger;
import java.util.Optional;


public class ProvenAIBlockchainUtil {
    private Web3j web3;
    private Credentials credentials;
    private String contractAddress;
//    private final ProvenAIAgentUsage contract;

    // Constructor for the utility class
    public ProvenAIBlockchainUtil(String url, String privateKey, String contractAddress) {
        this.web3 = Web3j.build(new HttpService(url));
        this.credentials = Credentials.create(privateKey);
        this.contractAddress = contractAddress;
        BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L);
        BigInteger gasLimit = BigInteger.valueOf(4_300_000);
        ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
//        this.contract = ProvenAIAgentUsage.load(contractAddress, web3, credentials, gasProvider);
    }

    // Function to record daily usage
    public String recordDailyUsage(BigInteger date, byte[] rootHash, BigInteger tokenSum) throws Exception {
//        TransactionReceipt transactionReceipt = contract.recordDailyUsage(date, rootHash, tokenSum).send();
//        return transactionReceipt.getTransactionHash();
        return null;
    }

    // Function to get daily usage
    public DailyUsageRoot getDailyUsage(String agentAddress, BigInteger date) throws Exception {
//        return contract.getDailyUsage(agentAddress, date).send();
        return null;
    }

    // Function to check if a transaction has been registered in the blockchain
    public boolean isTransactionRegistered(String transactionHash) throws Exception {
        Optional<TransactionReceipt> receipt = web3.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt();
        return receipt.isPresent() && "0x1".equals(receipt.get().getStatus());
    }

    // Nested class to map to the smart contract's DailyUsageRoot struct
    public static class DailyUsageRoot {
        public byte[] rootHash;
        public BigInteger tokenSum;
    }
}
