package dev.ctrlspace.provenai.backend.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BlockchainRegistrationDTO {

    private String ethereumAddress;
    private String did;
    private String merkleTreeRootHash;
    private String transactionHash;

}
