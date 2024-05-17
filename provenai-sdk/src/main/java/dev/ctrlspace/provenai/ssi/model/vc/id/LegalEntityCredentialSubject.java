package dev.ctrlspace.provenai.ssi.model.vc.id;

import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a legal entity credential subject.
 * <p>
 *     A legal entity credential subject is a collection of properties that describe a legal entity.
 *     It can be used to represent a legal entity's identity.
 * </p>
 * <p>
 *     This class is compliant with the W3C and EBSI v.2 Data Model of Verifiable Credentials specifications.
 *     @see <a href="https://code.europa.eu/ebsi/json-schema/-/tree/main/schemas/vcdm2.0/vid/legal-entity">EBSI v.2.0 VID Legal Entity</a>
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LegalEntityCredentialSubject implements CredentialSubject {
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
