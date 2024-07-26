package dev.ctrlspace.provenai.blockchain;

import com.bloxbean.cardano.client.crypto.Base58;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.blockchain.model.Counter;
import dev.ctrlspace.provenai.blockchain.model.ProvenAIAgentUsage;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
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
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;


public class ProvenAIBlockchainUtil {
    private Web3j web3;
    private Credentials credentials;
    private String contractAddress;
    private ProvenAIAgentUsage provenAIAgentUsageContract;
    private Counter counterContract;

    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";


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
     * Function to get the Ethereum address from a public JWK string
     *
     * @param publicJwkString The JWK string
     * @return The Ethereum address
     */
    public static String getEthereumAddressFromPublicJWK(String publicJwkString) {
        try {
            JSONObject jwk = new JSONObject(publicJwkString);
            byte[] xBytes = Base64.getUrlDecoder().decode(jwk.getString("x"));
            byte[] yBytes = Base64.getUrlDecoder().decode(jwk.getString("y"));

            byte[] publicKey = new byte[64];
            System.arraycopy(xBytes, 0, publicKey, 0, 32);
            System.arraycopy(yBytes, 0, publicKey, 32, 32);

            return getEthereumAddressFromPublicKey(publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Function to get the Ethereum address from a did:key string
     *
     * @param did The DID string
     * @return The Ethereum address
     */
    public static String getEthereumAddressFromDID(String did) {
        try {
            String multibaseValue = did.split(":")[2];
            byte[] decoded = decodeBase58(multibaseValue.substring(1));
            byte[] publicKey = Arrays.copyOfRange(decoded, decoded.length - 64, decoded.length);

            return getEthereumAddressFromPublicKey(publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Common function to get Ethereum address from public key bytes
     *
     * @param publicKey The public key bytes
     * @return The Ethereum address
     */
    private static String getEthereumAddressFromPublicKey(byte[] publicKey) {
        Keccak.Digest256 digest = new Keccak.Digest256();
        byte[] hash = digest.digest(publicKey);
        byte[] address = Arrays.copyOfRange(hash, 12, 32);
        return "0x" + Hex.toHexString(address);
    }

    private static byte[] decodeBase58(String input) {
        BigInteger bi = BigInteger.ZERO;
        for (char c : input.toCharArray()) {
            int index = ALPHABET.indexOf(c);
            if (index == -1) {
                throw new IllegalArgumentException("Invalid character in input: " + c);
            }
            bi = bi.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(index));
        }
        byte[] bytes = bi.toByteArray();
        // Remove leading zero byte if present
        return bytes[0] == 0 ? Arrays.copyOfRange(bytes, 1, bytes.length) : bytes;
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

    public static void main(String[] args) throws Exception {
        String privateJwkString = "{\"kty\":\"EC\",\"d\":\"Er2IRZg48uM1nn9PTVXRRXCYLt86EF5k7VPJP0X_dKs\",\"crv\":\"secp256k1\",\"kid\":\"yj1w1C4MSrRLql6VPcCuWpamUHsdBjPaXTq_9mcXFTU\",\"x\":\"E1-zYfaZzJ9R_59df2BwfdPh1a4cOxGQSrLYWuIEyzY\",\"y\":\"TYuSOxpuNlYU-w44c5-ASKTA5IYLfWLil6QpSOJTvDM\"}";
        String publicJwkString = "{\"kty\":\"EC\",\"crv\":\"secp256k1\",\"kid\":\"yj1w1C4MSrRLql6VPcCuWpamUHsdBjPaXTq_9mcXFTU\",\"x\":\"E1-zYfaZzJ9R_59df2BwfdPh1a4cOxGQSrLYWuIEyzY\",\"y\":\"TYuSOxpuNlYU-w44c5-ASKTA5IYLfWLil6QpSOJTvDM\"}";
        String did = "did:key:zdCru39GRVTj7Y6gKRbT9axbErpR9xAq9GmQmYcjoTE4gizNcRx6YqoA9JYf4KoZ6FUWViNBXuCBn5vNWxpEbPpaAyZ2qjas1C9KCGL3iVYNXRo9WNg33y4n9tYa";
//        String did = "did:key:z6MkuGtPR3mD2s2zhyv39QLZtNL2of8vTK8bwPR6ArARzo5J";


        String privateKey = ProvenAIBlockchainUtil.getEthereumPrivateKeyFromJWK(privateJwkString);
        String addressFromPublicJWK = ProvenAIBlockchainUtil.getEthereumAddressFromPublicJWK(publicJwkString);
        String addressFromPrivateJDK = ProvenAIBlockchainUtil.getEthereumAddressFromJwk(privateJwkString);
        String addressFromDidId = ProvenAIBlockchainUtil.getEthereumAddressFromDID(did);

        System.out.println("Ethereum Private Key: " + privateKey);
        System.out.println("Ethereum Address from Public JWK: " + addressFromPublicJWK);
        System.out.println("Ethereum Address: " + addressFromPrivateJDK);
        System.out.println("Ethereum Address from DID: " + addressFromDidId);
    }

    /**
     * Function to record daily usage for the account/agent address and date
     *
     * @param date     the date for which to record the daily usage
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

    public String updateDailyUsage(String agentAddress, BigInteger date, byte[] rootHash) throws Exception {
        TransactionReceipt transactionReceipt = provenAIAgentUsageContract.updateDailyUsageForAgent(agentAddress, date, rootHash).send();

        return transactionReceipt.getTransactionHash();
    }

    /**
     * Function to get the daily usage for a given agent address and date
     *
     * @param agentAddress the address of the agent
     * @param date         the date for which to get the daily usage
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
