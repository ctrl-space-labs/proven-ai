package dev.ctrlspace.provenai.backend.converters;

import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.OrganizationDTO;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OrganizationConverter implements ProvenAIConverter<Organization, OrganizationDTO>{

    @Override
    public OrganizationDTO toDTO(Organization organization) {
        OrganizationDTO organizationDTO = new OrganizationDTO();

        organizationDTO.setId(organization.getId());
        organizationDTO.setName(organization.getName());
        organizationDTO.setCountry(organization.getCountry());
        organizationDTO.setVatNumber(organization.getVatNumber());
        organizationDTO.setVerifiablePresentation(organization.getVerifiablePresentation());
        organizationDTO.setCreatedAt( organization.getCreatedAt());
        organizationDTO.setUpdatedAt(organization.getUpdatedAt());


        return organizationDTO;



    }

    @Override
    public Organization toEntity(OrganizationDTO organizationDTO) {
        Organization organization = new Organization();

        organization.setId(organizationDTO.getId());
        organization.setName(organizationDTO.getName());
        organization.setCountry(organizationDTO.getCountry());
        organization.setVatNumber(organizationDTO.getVatNumber());
        organization.setVerifiablePresentation(organizationDTO.getVerifiablePresentation());
        organization.setCreatedAt(organizationDTO.getCreatedAt());
        organization.setUpdatedAt(organizationDTO.getUpdatedAt());

        return organization;
    }



}
