package dev.ctrlspace.provenai.blockchain;

import io.vavr.collection.List;
import org.cardanofoundation.merkle.MerkleElement;
import org.cardanofoundation.merkle.MerkleTree;
import org.cardanofoundation.merkle.ProofItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Optional;

public class MerkleTreeUtil {

    public static void main(String[] args) {

        java.util.List<String> items = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            items.add(i + ". ");

            if (i % 10000 == 0) {
                System.out.println("Added:" + i);
            }
        }
        MerkleTreeUtil mtu = new MerkleTreeUtil();
        MerkleElement<Serializable> mt = mtu.createMerkleTree(items);

        System.out.println("Merkle tree with Size:" + mt.size() + " created!" );


        byte[] rootHash = mt.itemHash();
        System.out.println("Root Hash:" + HexFormat.of().formatHex(rootHash));


        String item = "782. ";

        Optional<List<ProofItem>> proof = mtu.getProof(mt, item);

        System.out.println("Proof:" + proof);

        boolean isValid = mtu.verifyProof(rootHash, item, proof.get());

        System.out.println("IsValidProof:" + isValid);
    }

    /**
     * Verify a proof for an item in the Merkle Tree
     * @param rootHash the root hash of the Merkle Tree
     * @param item the item to verify
     * @param proof the proof
     * @return true if the proof is valid
     */
    public boolean verifyProof(byte[] rootHash, Serializable item, List<ProofItem> proof) {
        boolean isValid = MerkleTree.verifyProof(rootHash, item, proof, MerkleTreeUtil::fromStringFun);
        return isValid;
    }

    /**
     * Generates a proof for an item in the Merkle Tree
     *
     * @param mt the Merkle Tree Root item
     * @param item the item to get the proof for
     * @return the proof
     */
    public Optional<List<ProofItem>> getProof(MerkleElement<Serializable> mt, String item) {
        Optional<List<ProofItem>> proof = MerkleTree.getProof(mt, item, MerkleTreeUtil::fromStringFun);
        return proof;
    }

    /**
     * Create a Merkle Tree from a list of items
     *
     * @param items the list of items
     * @return the Merkle Tree Root object
     */
    public  MerkleElement<Serializable> createMerkleTree(java.util.List<? extends Serializable> items) {

        return MerkleTree.fromList(List.ofAll(items), MerkleTreeUtil::fromStringFun);
    }

    public static byte[] fromStringFun(Serializable str) {
        return str.toString().getBytes();
    }
}
