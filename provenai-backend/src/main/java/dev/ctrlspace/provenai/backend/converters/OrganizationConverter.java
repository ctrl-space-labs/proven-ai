package dev.ctrlspace.provenai.backend.converters;

import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.OrganizationDTO;
import org.springframework.stereotype.Component;

@Component
public class OrganizationConverter implements ProvenAIConverter<Organization, OrganizationDTO> {

    @Override
    public OrganizationDTO toDTO(Organization organization) {
        OrganizationDTO organizationDTO = new OrganizationDTO();

        organizationDTO.setId(organization.getId());
        organizationDTO.setName(organization.getName());
        organizationDTO.setCountry(organization.getCountry());
        organizationDTO.setNaturalPerson(organization.getNaturalPerson());
        organizationDTO.setLegalPersonIdentifier(organization.getLegalPersonIdentifier());
        organizationDTO.setLegalName(organization.getLegalName());
        organizationDTO.setLegalAddress(organization.getLegalAddress());
        organizationDTO.setTaxReference(organization.getTaxReference());
        organizationDTO.setFamilyName(organization.getFamilyName());
        organizationDTO.setFirstName(organization.getFirstName());
        organizationDTO.setDateOfBirth(organization.getDateOfBirth());
        organizationDTO.setGender(organization.getGender());
        organizationDTO.setNationality(organization.getNationality());
        organizationDTO.setPersonalIdentifier(organization.getPersonalIdentifier());
        organizationDTO.setVatNumber(organization.getVatNumber());
        organizationDTO.setOrganizationDid(organization.getOrganizationDid());
        organizationDTO.setOrganizationVpJwt(organization.getOrganizationVpJwt());
        organizationDTO.setOrganizationDid(organization.getOrganizationDid());



        return organizationDTO;


    }

    @Override
    public Organization toEntity(OrganizationDTO organizationDTO) {
        Organization organization = new Organization();

        organization.setId(organizationDTO.getId());
        organization.setName(organizationDTO.getName());
        organization.setCountry(organizationDTO.getCountry());
        organization.setNaturalPerson(organizationDTO.getNaturalPerson());
        organization.setLegalPersonIdentifier(organizationDTO.getLegalPersonIdentifier());
        organization.setLegalName(organizationDTO.getLegalName());
        organization.setLegalAddress(organizationDTO.getLegalAddress());
        organization.setTaxReference(organizationDTO.getTaxReference());
        organization.setFamilyName(organizationDTO.getFamilyName());
        organization.setFirstName(organizationDTO.getFirstName());
        organization.setDateOfBirth(organizationDTO.getDateOfBirth());
        organization.setGender(organizationDTO.getGender());
        organization.setNationality(organizationDTO.getNationality());
        organization.setPersonalIdentifier(organizationDTO.getPersonalIdentifier());
        organization.setVatNumber(organizationDTO.getVatNumber());

        // Handle potential null values
        organization.setOrganizationDid(organizationDTO.getOrganizationDid() != null ? organizationDTO.getOrganizationDid() : null);
        organization.setOrganizationVpJwt(organizationDTO.getOrganizationVpJwt() != null ? organizationDTO.getOrganizationVpJwt() : null);



        return organization;
    }


}
