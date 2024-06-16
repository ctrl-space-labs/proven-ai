package dev.ctrlspace.provenai.backend.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AclPolicies;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import dev.ctrlspace.provenai.backend.model.PolicyType;
import dev.ctrlspace.provenai.backend.model.dtos.AclPoliciesDTO;
import dev.ctrlspace.provenai.backend.repositories.DataPodRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyOptionRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AclPoliciesConverter implements ProvenAIConverter<AclPolicies, AclPoliciesDTO> {

    private DataPodRepository dataPodRepository;
    private PolicyOptionRepository policyOptionRepository;
    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    public AclPoliciesConverter(DataPodRepository dataPodRepository,
                                PolicyOptionRepository policyOptionRepository,
                                PolicyTypeRepository policyTypeRepository) {
        this.dataPodRepository = dataPodRepository;
        this.policyOptionRepository = policyOptionRepository;
        this.policyTypeRepository = policyTypeRepository;
    }


    @Override
    public AclPoliciesDTO toDTO(AclPolicies aclPolicies) throws Exception, JsonProcessingException {
        AclPoliciesDTO dto = new AclPoliciesDTO();
        dto.setId(aclPolicies.getId());
        dto.setDataPodId(aclPolicies.getDataPod().getId());
        dto.setPolicyTypeId(aclPolicies.getPolicyType().getId());
        dto.setPolicyOptionId(aclPolicies.getPolicyOption().getId());
        dto.setValue(aclPolicies.getValue());
        dto.setCreatedAt(aclPolicies.getCreatedAt());
        dto.setUpdatedAt(aclPolicies.getUpdatedAt());
        dto.setCreatedBy(aclPolicies.getCreatedBy());
        dto.setUpdatedBy(aclPolicies.getUpdatedBy());

        return dto;
    }

    @Override
    public AclPolicies toEntity(AclPoliciesDTO aclPoliciesDTO) throws ProvenAiException {
        AclPolicies aclPolicies = new AclPolicies();
        aclPolicies.setId(aclPoliciesDTO.getId());

        DataPod dataPod = new DataPod();
        dataPod = dataPodRepository.findById(aclPoliciesDTO.getDataPodId())
                .orElseThrow(() -> new ProvenAiException("DATA_POD_NOT_FOUND","Data Pod not found", HttpStatus.NOT_FOUND));
        aclPolicies.setDataPod(dataPod);

        PolicyType policyType = new PolicyType();
        policyType = policyTypeRepository.findById(aclPoliciesDTO.getPolicyTypeId())
                .orElseThrow(() -> new ProvenAiException("POLICY_TYPE_NOT_FOUND","Policy Type not found", HttpStatus.NOT_FOUND));
        aclPolicies.setPolicyType(policyType);

        PolicyOption policyOption = new PolicyOption();
        policyOption = policyOptionRepository.findById(aclPoliciesDTO.getPolicyOptionId())
                .orElseThrow(() -> new ProvenAiException("POLICY_OPTION_NOT_FOUND","Policy Option not found", HttpStatus.NOT_FOUND));
        aclPolicies.setPolicyOption(policyOption);

        aclPolicies.setValue(aclPoliciesDTO.getValue());
        aclPolicies.setCreatedAt(aclPoliciesDTO.getCreatedAt());
        aclPolicies.setUpdatedAt(aclPoliciesDTO.getUpdatedAt());
        aclPolicies.setCreatedBy(aclPoliciesDTO.getCreatedBy());
        aclPolicies.setUpdatedBy(aclPoliciesDTO.getUpdatedBy());

        return aclPolicies;
    }
}
