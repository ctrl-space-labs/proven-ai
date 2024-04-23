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
