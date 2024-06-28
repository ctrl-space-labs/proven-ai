package dev.ctrlspace.provenai.backend.model;

import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.id.LegalEntityCredentialSubject;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "organizations", schema = "proven_ai")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization {
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "country")
    private String country;
    @Basic
    @Column(name = "is_natural_person")
    private Boolean isNaturalPerson;
    @Basic
    @Column(name = "legal_person_identifier")
    private String legalPersonIdentifier;
    @Basic
    @Column(name ="legal_name")
    private String legalName;
    @Basic
    @Column(name = "legal_address")
    private String legalAddress;
    @Basic
    @Column(name = "tax_reference")
    private String taxReference;
    @Basic
    @Column(name = "family_name")
    private String familyName;
    @Basic
    @Column(name = "first_name")
    private String firstName;
    @Basic
    @Column(name ="date_of_birth")
    private Instant dateOfBirth;
    @Basic
    @Column(name = "gender")
    private String gender;
    @Basic
    @Column(name = "nationality")
    private String nationality;
    @Basic
    @Column(name ="personal_identifier")
    private String personalIdentifier;

    @Basic
    @Column(name = "vat_number")
    private String vatNumber;


    @Basic
    @Column(name = "organization_vp_jwt")
    private String organizationVpJwt;

    @Basic
    @Column(name = "organization_did")
    private String organizationDid;

    @Column(name = "created_at")
    private Instant createdAt;
    @Basic
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Basic
    @Column(name = "created_by")
    private UUID createdBy;
    @Basic
    @Column(name = "updated_by")
    private UUID updatedBy;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getOrganizationVpJwt() {return organizationVpJwt;}

    public void setOrganizationVpJwt(String organizationVpJwt) {this.organizationVpJwt = organizationVpJwt;}

    public String getOrganizationDid() {
        return organizationDid;
    }

    public void setOrganizationDid(String organizationDid) {
        this.organizationDid = organizationDid;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

//    public void setNaturalPerson(Boolean naturalPerson) {
//        isNaturalPerson = naturalPerson;
//    }

    public void setNaturalPerson(Boolean naturalPerson) {
        isNaturalPerson = naturalPerson;
    }

    public Boolean getNaturalPerson() {
        return isNaturalPerson;
    }

    public String getLegalPersonIdentifier() {
        return legalPersonIdentifier;
    }

    public void setLegalPersonIdentifier(String legalPersonIdentifier) {
        this.legalPersonIdentifier = legalPersonIdentifier;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getTaxReference() {
        return taxReference;
    }

    public void setTaxReference(String taxReference) {
        this.taxReference = taxReference;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPersonalIdentifier() {
        return personalIdentifier;
    }

    public void setPersonalIdentifier(String personalIdentifier) {
        this.personalIdentifier = personalIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(country, that.country)) return false;
        if (!Objects.equals(isNaturalPerson, that.isNaturalPerson))
            return false;
        if (!Objects.equals(legalPersonIdentifier, that.legalPersonIdentifier))
            return false;
        if (!Objects.equals(legalName, that.legalName)) return false;
        if (!Objects.equals(legalAddress, that.legalAddress)) return false;
        if (!Objects.equals(taxReference, that.taxReference)) return false;
        if (!Objects.equals(familyName, that.familyName)) return false;
        if (!Objects.equals(firstName, that.firstName)) return false;
        if (!Objects.equals(dateOfBirth, that.dateOfBirth)) return false;
        if (!Objects.equals(gender, that.gender)) return false;
        if (!Objects.equals(nationality, that.nationality)) return false;
        if (!Objects.equals(personalIdentifier, that.personalIdentifier))
            return false;
        if (!Objects.equals(vatNumber, that.vatNumber)) return false;
        if (!Objects.equals(organizationVpJwt, that.organizationVpJwt))
            return false;
        if (!Objects.equals(organizationDid, that.organizationDid))
            return false;
        if (!Objects.equals(createdAt, that.createdAt)) return false;
        if (!Objects.equals(updatedAt, that.updatedAt)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        return Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (isNaturalPerson != null ? isNaturalPerson.hashCode() : 0);
        result = 31 * result + (legalPersonIdentifier != null ? legalPersonIdentifier.hashCode() : 0);
        result = 31 * result + (legalName != null ? legalName.hashCode() : 0);
        result = 31 * result + (legalAddress != null ? legalAddress.hashCode() : 0);
        result = 31 * result + (taxReference != null ? taxReference.hashCode() : 0);
        result = 31 * result + (familyName != null ? familyName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (nationality != null ? nationality.hashCode() : 0);
        result = 31 * result + (personalIdentifier != null ? personalIdentifier.hashCode() : 0);
        result = 31 * result + (vatNumber != null ? vatNumber.hashCode() : 0);
        result = 31 * result + (organizationVpJwt != null ? organizationVpJwt.hashCode() : 0);
        result = 31 * result + (organizationDid != null ? organizationDid.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        return result;
    }
}
