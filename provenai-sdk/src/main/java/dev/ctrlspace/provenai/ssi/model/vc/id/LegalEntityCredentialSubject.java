package dev.ctrlspace.provenai.ssi.model.vc.id;

import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
class LegalEntityCredentialSubject extends CredentialSubject {
    private String id;
    private String legalPersonIdentifier;
    private String legalName;
    private String legalAddress;
    private String VATRegistration;
    private String taxReference;
    private String LEI;
    private String EORI;
    private String SEED;
    private String SIC;
    private Object domainName; // Can be String or List<String>

}
