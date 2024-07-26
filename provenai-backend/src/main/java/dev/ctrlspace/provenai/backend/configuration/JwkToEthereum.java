package dev.ctrlspace.provenai.backend.configuration;

import dev.ctrlspace.provenai.blockchain.ProvenAIBlockchainUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.security.Security;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class JwkToEthereum {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Main method to test the conversion of a JWK to an Ethereum private key and address
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // I am committing the Ganache private key to the repository for demonstration purposes only
        ProvenAIBlockchainUtil blockchainUtil = new ProvenAIBlockchainUtil("HTTP://127.0.0.1:7545",
                "0x99c026d99302999c57c4de1646e1ead4da58e0b753b7f8a3392269e71c2ddd0d",
                "0xdA7B2e713d7b3674f2f90Ed14C1100188C91a6cd",
                1337);

        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate yesterday = today.minusDays(1);

        // Convert the current date to a ZonedDateTime at the start of the day in UTC
        ZonedDateTime startOfDay = yesterday.atStartOfDay(ZoneOffset.UTC);

        // Convert the ZonedDateTime to an Instant
        Instant startOfDayInstant = startOfDay.toInstant();
        byte[] merkleRoot = "This is a string that is byte-32".getBytes();
        String transactionHash = blockchainUtil.recordDailyUsage(BigInteger.valueOf(startOfDayInstant.toEpochMilli()), merkleRoot);

        System.out.println("Transaction Hash: " + transactionHash);

        byte[] secondMerkleRoot = "Th1s 1s a str1ng that 1s byte-32".getBytes();
        String updatedTransactionHash = blockchainUtil.updateDailyUsage("0xB4fe2CD2390e24772bd7c22496Eaf9e2179daE87",
                BigInteger.valueOf(startOfDayInstant.toEpochMilli()),
                secondMerkleRoot);


        System.out.println("Updated Transaction Hash: " + updatedTransactionHash);


    }
}