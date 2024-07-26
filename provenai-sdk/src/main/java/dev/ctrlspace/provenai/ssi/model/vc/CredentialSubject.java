package dev.ctrlspace.provenai.ssi.model.vc;

/**
 * Class to represent the 'credentialSubject' part of the JSON
 * This class is abstract and should be extended by the specific credential subject
 * This class represents the root of the JSON object that represents a Verifiable Credential.
 * Specific types of Verifiable Credentials should extend this class and add their own properties.
 * e.g. a Legal Entity Verifiable Credential should extend this class and add properties like legalName, VAT Number, etc.
 */
public interface CredentialSubject {
    public abstract String getId();
}

