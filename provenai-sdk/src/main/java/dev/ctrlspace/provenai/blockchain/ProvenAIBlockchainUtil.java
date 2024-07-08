package dev.ctrlspace.provenai.blockchain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.blockchain.model.Counter;
import dev.ctrlspace.provenai.blockchain.model.ProvenAIAgentUsage;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Optional;


public class ProvenAIBlockchainUtil {
    private Web3j web3;
    private Credentials credentials;
    private String contractAddress;
    private ProvenAIAgentUsage provenAIAgentUsageContract;
    private Counter counterContract;

    // Constructor for the utility class
    public ProvenAIBlockchainUtil(String url, String privateKey, String contractAddress, long chainId) throws IOException {
        this.web3 = Web3j.build(new HttpService(url));
        this.credentials = Credentials.create(privateKey);
        this.contractAddress = contractAddress;
        EthGasPrice ethGasPrice = web3.ethGasPrice().send();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(60_000L);
        ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
        TransactionManager transactionManager = new RawTransactionManager(web3, credentials, chainId);
        this.provenAIAgentUsageContract = ProvenAIAgentUsage.load(contractAddress, web3, transactionManager, gasProvider);
    }

    /**
     * Function to get the Ethereum private key from a JWK string
     * This can be used also when a did:key is using the secp256k1 curve
     *
     * @param jwkString
     * @return
     * @throws Exception
     */
    public static String getEthereumPrivateKeyFromJWK(String jwkString) throws Exception {
        // Parse the JWK string to extract the private key
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jwkNode = objectMapper.readTree(jwkString);

        String dBase64Url = jwkNode.get("d").asText();

        while (dBase64Url.length() % 4 != 0) {
            dBase64Url += "=";
        }
        // Decode base64url to byte array
        byte[] dBytes = Base64.getUrlDecoder().decode(dBase64Url);

        // Convert to BigInteger
        BigInteger d = new BigInteger(1, dBytes);

        // Get Ethereum private key in hex format
        String privateKey = d.toString(16);

        // Ensure the private key is 64 characters long by padding with leading zeros if necessary
        while (privateKey.length() < 64) {
            privateKey = "0" + privateKey;
        }
        return privateKey;
    }

    /**
     * Function to get the Ethereum address from a JWK string
     *
     * @param jwtPrivateKey
     * @return
     * @throws Exception
     */
    public static String getEthereumAddressFromJwk(String jwtPrivateKey) throws Exception {
        String ethereumPrivateKey = getEthereumPrivateKeyFromJWK(jwtPrivateKey);
        return ProvenAIBlockchainUtil.getEthereumAddress(ethereumPrivateKey);
    }

    /**
     * Function to get the Ethereum address from a private key
     *
     * @param privateKey
     * @return
     */
    public static String getEthereumAddress(String privateKey) {
        return Credentials.create(privateKey).getAddress();
    }

    /**
     * Function to record daily usage for the account/agent address and date
     * @param date the date for which to record the daily usage
     * @param rootHash the Merkle Root hash of the daily usage
     * @return the transaction hash
     * @throws Exception
     */
    public String recordDailyUsage(BigInteger date, String rootHash) throws Exception {
        return this.recordDailyUsage(date, rootHash.getBytes());
    }

    // Function to record daily usage
    public String recordDailyUsage(BigInteger date, byte[] rootHashBytes) throws Exception {
        TransactionReceipt transactionReceipt = provenAIAgentUsageContract.recordDailyUsage(date, rootHashBytes).send();

        return transactionReceipt.getTransactionHash();
    }

    public String updateDailyUsage(String agentAddress,  BigInteger date, byte[] rootHash) throws Exception {
        TransactionReceipt transactionReceipt = provenAIAgentUsageContract.updateDailyUsageForAgent(agentAddress, date, rootHash).send();

        return transactionReceipt.getTransactionHash();
    }

    /**
     * Function to get the daily usage for a given agent address and date
     *
     * @param agentAddress the address of the agent
     * @param date the date for which to get the daily usage
     * @return the Merkle Root hash of the daily usage
     * @throws Exception
     */
    public String getDailyUsage(String agentAddress, BigInteger date) throws Exception {
        byte[] hashBytes = provenAIAgentUsageContract.getDailyUsage(agentAddress, date).send();
        return new String(hashBytes);
    }

    // Function to get the counter value
    public BigInteger getCounterValue() throws Exception {
        return counterContract.getNumber().send();
    }

    // Function to increment the counter value
    public String incrementCounter() throws Exception {
        return counterContract.increment().send().getTransactionHash();
    }

    // Function to set the counter value
    public String setCounterValue(BigInteger value) throws Exception {

        return counterContract.setNumber(value).send().getTransactionHash();
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
