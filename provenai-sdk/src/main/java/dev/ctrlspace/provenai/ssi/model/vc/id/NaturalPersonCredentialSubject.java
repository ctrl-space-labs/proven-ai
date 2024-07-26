package dev.ctrlspace.provenai.ssi.model.vc.id;

import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Represents a natural person credential subject.
 * <p>
 *     A natural person credential subject is a collection of properties that describe a natural person.
 *     It can be used to represent a person's identity.
 * </p>
 * <p>
 *     This class is compliant with the W3C and EBSI v.2 Data Model of Verifiable Credentials specifications.
 *     @see <a href="https://code.europa.eu/ebsi/json-schema/-/tree/main/schemas/vcdm2.0/vid/natural-person">EBSI v.2.0 VID Natural Person</a>
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NaturalPersonCredentialSubject implements CredentialSubject {
    private String id;
    private String familyName;
    private String firstName;
    private String familyNameAtBirth;
    private String firstNameAtBirth;
    private Instant dateOfBirth;
    private int yearOfBirth;
    private boolean ageOverNN;
    private int ageInYears;
    private String personalIdentifier;
    private Address placeOfBirth;
    private Address currentAddress;
    private String gender;
    private List<String> nationality;

}
