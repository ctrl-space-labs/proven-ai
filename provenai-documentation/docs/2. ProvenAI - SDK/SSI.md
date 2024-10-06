This package contains the main classes for the SSI (Self-Sovereign Identity) module.
In the context of ProvenAI, the SSI module is used to generate:
- AI Agent's DID
- Issue AI Agent's ID VC
- Present the Agent's ID VC
- Issue the "Permission to Use" VC of a document, to be used by the AI Agent.
- Verify VCs like the above (Agent's ID VC, Permission to Use VC)

### Converters
The `converters` package contains necessary converters for the SDK's functionalities. In the context of provenAI SDK the converters are responsible for transforming all the verifiable formats supported by provenAI in order to adhere to the W3CVC [https://www.w3.org/TR/vc-data-model/] standard. The converters developed are:
- `AgentIDConverter`
- `DataOwnershipCredentialConverter`
- `LegalEntityConverter`
- `NaturalPersonConverter`
- `PermissionOfUseConverter`

### Issuer 
The issuer package contains all the classes and methods responsible to issue Verifiable Credentials supported by provenAI. 

#### Credential Issuance API

This class sends a request to an OID4VC compliant, credential issuance API in order to issue a signed credential. In provenAI's context the WaltID issuer API is used []. The credentian issuance api functionality is described in [section].

# Key Creation

The `KeyCreation` class provides a method for generating cryptographic key pairs based on a given `KeyType` and character length using the Walt.id library.

## generateKey


```java
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;

public class Main {
    public static void main(String[] args) {

        JWKKey jwkKey = KeyCreation.generateKey(KeyType.secp256k1, characterLength);
        
        System.out.println("Generated Key ID: " + jwkKey.getKeyId());
    }
}
```
The `generateKey()` method creates a new key pair based on a given KeyType and character length. In the `KeyCreation` class also the `JWKKeyMetadata()` is defined. The  `JWKKeyMetadata()` is an instance of `JwkKeyMeta` and receives as input the `keyId` and the key `characterLength`. The supported keyId values as provided in the waltid crypto are: `JwkKey`, `TSEKey` and `OCIKey`. In the context of provenAI `JwkKey` keys are issued. To generate the key the method generateBlocking  with inputs the keyType and the `JwkKeyMetadata` is used from waltID's `JWKkey` kotlin class. 

```java
JwkKeyMeta metadata = new JwkKeyMeta("JWKKey",characterLength);
```
The supported key types are: `Ed25519`, `secp256k1`, `secp256r1` and `RSA`. In provenAI context we mainly work with `secp256k1` because this format is supported by the blockchain environment we use [edw tha to grapsw kalitera sto telos].


## JWKKeyWrapper
The `JWKKeyWrapper` class is created to wrap the waltID crypto JWKKey class methods in java, because it is written in kotlin. This ensures interoperability of this kotlin class in Java.

```java
import id.walt.crypto.keys.jwk.JWKKey;
import id.walt.crypto.keys.KeyType;
import dev.ctrlspace.provenai.ssi.issuer.JWKKeyWrapper;
import id.walt.crypto.KeyCreation;


public class Main {
    public static void main(String[] args) {

        public static void main(String[] args) {
        // Initialize the JWKKeyWrapper
        JWKKeyWrapper jwkKeyWrapper = new JWKKeyWrapper();

        int characterLength = 2048; 
        JWKKey jwkKey = KeyCreation.generateKey(KeyType.secp256r1, characterLength);

        // Export the key to JWK format
        Object exportedJWK = jwkKeyWrapper.exportJWK(jwkKey);
        System.out.println("Exported JWK: " + exportedJWK);

        // Get the public part of the key
        JWKKey publicKey = jwkKeyWrapper.getPublicKey(jwkKey);
        System.out.println("Public Key: " + publicKey);

        // Retrieve the key ID of the JWK key
        Object keyId = jwkKeyWrapper.getKeyId(jwkKey);
        System.out.println("Key ID: " + keyId);

        // Check if the key has a private part
        Boolean hasPrivateKey = jwkKeyWrapper.hasPrivateKey(jwkKey);
        System.out.println("Has Private Key: " + hasPrivateKey);

        // Get the key type 
        KeyType keyType = jwkKeyWrapper.getKeyType(jwkKey);
        System.out.println("Key Type: " + keyType);

        // Get the JWK string representation of the key
        String jwkString = jwkKeyWrapper.getJwk(jwkKey);
        System.out.println("JWK String: " + jwkString);

        // Export the key to PEM format
        Object exportedPem = jwkKeyWrapper.exportPem(jwkKey);
        System.out.println("Exported PEM: " + exportedPem);

        // Get the public key representation 
        Object publicKeyRepresentation = jwkKeyWrapper.getPublicKeyRepresentation(jwkKey);
        System.out.println("Public Key Representation: " + publicKeyRepresentation);

        // Retrieve the thumbprint of the JWK key 
        Object thumbprint = jwkKeyWrapper.getThumbprint(jwkKey);
        System.out.println("Thumbprint: " + thumbprint);

    }
    }
}
```



#### DID Issuer

The DidIssuer class provides methods for creating and resolving Decentralized Identifiers (DIDs) using various key types and methods. It leverages the Walt ID services for DID management and includes functionality for both key-based and web-based DIDs. For this class's methods we leverage the `WaltIdServiceInitUtils` methods from WaltID identity package and wrap the kotlin developed methods in Java to be able to use them in a springboot project. 
With the `DidIssuer` constructor  the `WaltIdServiceInitUtils` is initialized, to access its methods. Also it initialized the necessary continuation object for Kotlin coroutines. [ref gia coroutines apo utils]

`WaltIdServiceInitUtils.INSTANCE.initializeWaltIdServices();`

##### Methods
- **createDidFromKey**: Creating a DID from a Key Pair

This method creates a Decentralized Identifier (DID) from a provided key pair using the did:key method.

```java
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;

public class Main {
    public static void main(String[] args) {
        DidIssuer didIssuer = new DidIssuer();
        JWKKey jwkKey = KeyCreation.generateKey(KeyType.RSA, characterLength);
        DidResult didResult = didIssuer.createDidFromKey(KeyType.RSA, jwkKey);
        
        System.out.println("Created DID: " + didResult.getDid());
    }
}
```

After generating a key we can issue the DID generated from the key. The method `createdDidFromKey()` from `DidIssuer`. It creates a DID from the generated key (`jwkKey`) and the provided `KeyType`. The object returned is a `DidResult`. The `DidKeyCreateOptions` object must be initialized, tahat defines the parameters for the DID created. The necessary inputs are the `keyType` and the boolean `useJwkJcsPub`. If the `useJwkJcsPub` flag is set to true, it applies the EBSI-specific jwk_jcs-pub encoding during the resolution process. To generate the DID, the method `registerByKey` is used from `DidService` The inputs needed is the generation method, dependent on the key provided, the key JWK, the `DidKeyCreateOptions` and a continuation object for kotlin coroutines. In the contect of provenAI the method is se to `"key"`.

- **createDidFromAutoKey**: This method creates a Decentralized Identifier (DID) using the did:key method with a key that is generated internally. You specify the key type and the method handles the key creation.


```java
import id.walt.crypto.keys.KeyType;

public class Main {
    public static void main(String[] args) {
        DidIssuer didIssuer = new DidIssuer();
        DidResult didResult = didIssuer.createDidFromAutoKey(KeyType.RSA);
        
        System.out.println("Created DID: " + didResult.getDid());
    }
}
```
The method `createdDidFromKey()` from `DidIssuer`. It creates a DID from the generated key (`jwkKey`) and the provided `KeyType`. The object returned is a `DidResult`. The `DidKeyCreateOptions` object must be initialized, tahat defines the parameters for the DID created. The necessary inputs are the `keyType` and the boolean `useJwkJcsPub`. If the `useJwkJcsPub` flag is set to true, it applies the EBSI-specific jwk_jcs-pub encoding during the resolution process. To generate the DID, the method `registerByKey` is used from `DidService` The inputs needed is the generation method, dependent on the key provided, the key JWK, the `DidKeyCreateOptions` and a continuation object for kotlin coroutines. In the contect of provenAI the method is se to `"key"`.


- **createDidFromWeb**
```java
public class Main {
    public static void main(String[] args) {
        DidIssuer didIssuer = new DidIssuer();
        DidResult didResult = didIssuer.createDidFromWeb("example.com", "/did/doc", KeyType.RSA);
        
        System.out.println("Created DID: " + didResult.getDid());
    }
}

```
The method `createDidFromWeb()` from `DidIssuer`. It creates a DID from the `domain` that serves the did document and the `path` it is located. The object returned is a `DidResult`. The `DidWebCreateOptions` object must be initialized, tahat defines the parameters for the DID created. The necessary inputs are the `domain`, the `path` and the `KeyType`. To generate the DID, the method `registerBlocking` is used from `DidService` The input needed is the `DidWebCreateOptions`.

- **resolveKeyDidToKey**: This method resolves a key-based DID to its key. This method accepts a key type and resolves the given JSON Web Key (JWK) to its associated DID.

```java
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;

public class Main {
    public static void main(String[] args) {

        DidIssuer didIssuer = new DidIssuer();
        
        // Generate a JWKKey
        JWKKey jwkKey = KeyCreation.generateKey(KeyType.secp256k1, characterLength);

        DidResult didResult = didIssuer.resolveKeyDidToKey(KeyType.secp256k1, false, jwkKey);

        System.out.println("Resolved Key DID: " + didResult.getDid());
    }
}

```

After generating a key we can resolve the DID generated to that key. The method used is `resolveKeyDidToKey()` from `DidIssuer`. It resolves a DID from the generated key (`jwkKey`) and the provided `KeyType` and the flag `useJwkJcsPub`. The object returned is a `DidResult`. The `DidKeyCreateOptions` object must be initialized. The necessary inputs are the `keyType` and the boolean `useJwkJcsPub`. To resolve the DID, the method `createByKeyBlocking` is used from `LocalRegistrar` The inputs needed is the generation method, dependent on the key provided, the key JWK, the `DidKeyCreateOptions` and they key JWK.

- **resolveWebDidToKey**: This method resolves a web-based DID to its key and resolves the given JSON Web Key (JWK) to its associated DID.

```java
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;

public class Main {
    public static void main(String[] args) {

DidIssuer didIssuer = new DidIssuer();
        
        JWKKey jwkKey = KeyCreation.generateKey(KeyType.secp256k1, characterLength);

        // Resolve the DID to the key using web-based DID
        DidResult didResult = didIssuer.resolveWebDidToKey(KeyType.secp256k1, "example.com", "/did-path", jwkKey);

        // Output the resolved Web DID
        System.out.println("Resolved Web DID: " + didResult.getDid());
    }
}
```

After generating a key we can resolve the DID generated to that key. The method used is `resolveWebDidToKey()` from `DidIssuer`. It resolves a DID from the generated key (`jwkKey`), the provided `domain` and `path`. The object returned is a `DidResult`. The `DidWebCreateOptions` object must be initialized. To resolve the DID, the method `createByKeyBlocking` is used from `LocalRegistrar` The inputs needed is the generation method, dependent on the key provided, the key JWK, the `DidWebCreateOptions` and they key JWK.

#### ProvenAIIssuer

#### VerifiableCredentialBuilder

The `VerifiableCredentialBuilder` class provides a flexible way to create verifiable credentials in compliance with the W3C Verifiable Credentials (VC) standard. It allows users to define the context, types, issuer, validity, subject, and other properties of the credential, and provides a method to sign the credential with a key. To generate the verifiable credential this class wraps the methods developed in WaltID's kotlin class `CredentialBuilder`. Hence we need to initialize the credential builder setting the `credentialBuilderType` to `W3CV11CredentialBuilder`, in the `VerifiableCredentialBuilder` constructor. An example implementation is presented below.


```java
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.JWKKey;
import id.walt.crypto.keys.KeyType;
import kotlin.Pair;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import kotlinx.serialization.json.JsonPrimitive;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Initialize DidIssuer to generate DID for issuer and subject
        DidIssuer didIssuer = new DidIssuer();

        // Generate a JWK key for the issuer
        JWKKey issuerKey = KeyCreation.generateKey(KeyType.secp256k1, 2048);
        DidResult issuerDidResult = didIssuer.createDidFromKey(KeyType.secp256k1, issuerKey);
        String issuerDid = issuerDidResult.getDid();  // Get the DID of the issuer

        // Generate a JWK key for the subject
        JWKKey subjectKey = KeyCreation.generateKey(KeyType.secp256k1, 2048);
        DidResult subjectDidResult = didIssuer.createDidFromKey(KeyType.secp256k1, subjectKey);
        String subjectDid = subjectDidResult.getDid();  // Get the DID of the subject

        System.out.println("Issuer DID: " + issuerDid);
        System.out.println("Subject DID: " + subjectDid);

        // Initialize the verifiable credential builder
        VerifiableCredentialBuilder vcBuilder = new VerifiableCredentialBuilder();

        // Add context 
        vcBuilder.addContext("https://www.w3.org/2018/credentials/v1");

        // Add types to the verifiable credential
        vcBuilder.addType("VerifiableCredential");
        vcBuilder.addType("EBSILegalEntityVerifiableID");

        // Set the credential ID
        vcBuilder.setCredentialId("urn:uuid:12345678-abcd-1234-abcd-1234567890ab");

        // Set the issuer's DID
        vcBuilder.setIssuerDid(issuerDid);

        // Set the validity period
        vcBuilder.validFromNow();  
        vcBuilder.validFor(Duration.ofDays(365));  

        // Define the credential subject 
        JsonObject subjectData = new JsonObject(Map.of(
            "id", new JsonPrimitive(subjectDid),  
            "legalName", new JsonPrimitive("Example Corp")
        ));
        vcBuilder.credentialSubject(subjectData);

        // Set the subject's DID
        vcBuilder.setSubjectDid(subjectDid);

        // Add extra data to the credential
        vcBuilder.useData(new Pair<>("someKey", new JsonPrimitive("someValue")));

        // Build the verifiable credential
        W3CVC vc = vcBuilder.buildCredential();

        // Sign the verifiable credential
        Map<String, String> additionalJwtHeaders = new HashMap<>();  // Additional JWT headers
        Map<String, JsonElement> additionalJwtOptions = new HashMap<>();  // Additional JWT options

        Object signedVc = vcBuilder.signCredential(
            vc, 
            issuerKey,  // Use the issuer's key for signing
            issuerDid,  // Issuer DID
            subjectDid,  // Subject DID
            additionalJwtHeaders, 
            additionalJwtOptions
        );

        // Output the signed verifiable credential
        System.out.println("Signed Verifiable Credential: " + signedVc.toString());
    }
}
```
#### VerifiablePresentationBuilder

The `VerifiablePresentationBuilder` class provides a flexible way to create a verifiable credential(s) presentation in compliance with the W3C standard. It allows users to define the subject, the nonce generation method the credential and the selection of how many and which credentials to include in the presentation. It and provides a method to sign the credential with a key. To generate the verifiable presentation this class wraps the methods developed in WaltID's kotlin class `PresentationBuilder`. Hence we need to initialize the presentation builder, in the `VerifiablePresentationBuilder` constructor. An example implementation is presented below.

```java
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonPrimitive;
import id.walt.crypto.keys.JWKKey;

public class Main {
    public static void main(String[] args) {
        
        Object signedVc = vcBuilder.signCredential(
            vc, 
            issuerKey,  // Use the issuer's key for signing
            issuerDid,  // Issuer DID
            subjectDid,  // Subject DID
            additionalJwtHeaders, 
            additionalJwtOptions
        );

        // Generate a JWK key for the subject
        JWKKey subjectKey = KeyCreation.generateKey(KeyType.secp256k1, 2048);

        DidResult subjectDidResult = didIssuer.createDidFromKey(KeyType.secp256k1, subjectKey);

        String subjectDid = subjectDidResult.getDid();  // Get the DID of the subject

        // Set Subject DID
        vpBuilder.setDid(subjectDid);

        // Set unique presentation ID
        vpBuilder.setPresentationId();

        // Set a nonce for the presentation (example)
        vpBuilder.setNonce("random_nonce");

        // Add the signed Verifiable Credential (VC) to the Verifiable Presentation (VP)
        vpBuilder.addCredential(new JsonPrimitive(signedVc.toString()));

        // Build the Verifiable Presentation JSON
        JsonElement presentationJson = vpBuilder.buildPresentationJson();
        System.out.println("Verifiable Presentation JSON: " + presentationJson.toString());

        // Sign the Verifiable Presentation (VP) with the subject's key
        Object signedPresentation = vpBuilder.buildAndSign(subjectKey);
        System.out.println("Signed Verifiable Presentation: " + signedPresentation.toString());
    }
}

```

### Verifiable Credential Model 
This section provides documentation for the core classes representing Verifiable Credentials (VC) as defined by the W3C Verifiable Credentials Data Model. he `CredentialSubject` interface defines the structure for the credentialSubject part of any Verifiable Credential. It includes an abstract method `getId()` which must be implemented by any specific credential subject. The `VerifiableCredential` class is a generic class used to define the structure of a Verifiable Credential. It is parameterized with `T`, where `T` must be a class that extends the `CredentialSubject` interface. 

#### Schemas

This package defines schemas for Verifiable Credentials (VCs) issued in the provenAI context, natural person, legal entity, data ownership, and AI agent. These JSON schemas are used to standardize how information is represented in verifiable credentials within decentralized and verifiable systems, such as those leveraging Distributed Ledger Technology (DLT) or Decentralized Identifiers (DIDs). Each schema complies with the JSON Schema Draft 2020-12 standard.

1. `natural-person.schema.json`

    Purpose: Defines a Verifiable Credential (VC) for a natural person.
    Required Fields:
        id: Unique identifier of the credential subject.
        familyName: The family name of the subject.
        firstName: The first name of the subject.
        dateOfBirth: Date of birth (formatted as date).

2. `legal-entity.schema.json`

    Purpose: Defines a Verifiable Credential for legal entities, such as businesses or organizations.
    Required Fields:
        id: Unique identifier of the credential subject.
        legalName: Official legal name of the entity.
        domaiName: Domain name of Credential Subject.

3. `data-ownership.schema.json`

    Purpose: Describes a certificate that verifies the ownership of data by a data pod.
    Required Fields:
        dataPodName: Name of the data pod.
        dataPodId: Unique identifier for the data pod.
        ownershipStatus: Status of ownership (e.g., active or suspended).
        usagePolicies: An array of policies governing data usage.
        isccCollectionMerkleRoot: Merkle root for ISCC for a document section collection.

4. `verifiable-ai-agent.schema.json`

    Purpose: Defines a schema for verifying AI agents, which may act autonomously or as part of a larger system.
    Required Fields:
        id: Unique identifier of the credential subject.
        organizationName: Name of the organization responsible for the AI agent
        agentName: Name of the AI agent.
        creationDate: Date the AI agent was created.

5. `permission-of-use.schema.json`

    Purpose: Specifies the schema for permissions granted for the use of data or systems by an entity.
    Required Fields:
        id: Unique identifier of the credential subject.
        ownerID: Unique identifier (DID) of the data owner.
        policies: List of all policies applicable to the data usage by the agent.
        dataSegments: List of ISCCs that the agent has the rights to use.

#### Verifiable Credential Subjects

##### Attestation
This package contains classes representing the credential subject part of attestation Verifiable Credentials (VCs). Each class is compliant with the W3C and EBSI standards for Verifiable Credentials and is used to define the entities and their attributes for attestation purposes.

- `AIAgentCredentialSubject`
This class defines the credential subject for an AI Agent Credential. It includes properties such as the organizationName, agentName, agentId, and a creationDate that represents the timestamp when the agent was created. The class also holds a list of usagePolicies, representing policies that govern the use of the agent.

- `DataOwnershipCredentialSubject`
This class represents the credential subject for Data Ownership Credentials. It defines fields such as the dataPodName, dataPodId, and ownershipStatus to track data pod ownership. Additional fields include usagePolicies, which defines the policies applied to the data pod, and isccCollectionMerkleRoot, which links to the ISCC (International Standard Content Code) collection associated with the data.

- `PermissionOfUseCredentialSubject`
This class defines the credential subject for a Permission of Use Credential. It includes the id (representing the DID of the AI agent), ownerDID (representing the data owner's DID), and a list of policies that describe the permissions related to the credential. The dataSegments field holds a list of ISCCs representing specific data segments covered by the credential.

- `W3CCredentialSubject`
This class defines the credential subject for a W3C Verifiable Credential. It includes a useData field represented as a Pair of string and JsonElement, allowing flexible, additional data to be attached to the credential. The credentialSubject field stores the actual subject of the credential in JSON format, following the W3C VC standards.

##### ID
This package contains classes representing the credential subject part for ID Verifiable Credentials (VCs). Each class is compliant with the W3C and EBSI standards for Verifiable Credentials and is used to define the entities and their attributes for attestation purposes.

- `NaturalPersonCredentialSubject`
This class represents the credential subject for a natural person. It includes fields such as id (representing the DID of the person), familyName, firstName, and other personal identifiers like dateOfBirth, yearOfBirth, and personalIdentifier. The class also includes placeOfBirth and currentAddress fields, represented by the Address class. The nationality field is a list of the person’s nationalities.

- `LegalEntityCredentialSubject`
This class represents the credential subject for a legal entity. It includes fields like id (representing the DID of the legal entity), legalPersonIdentifier, legalName, and legalAddress. The class also contains fields such as VATRegistration, taxReference, and other legal identifiers like LEI (Legal Entity Identifier) and EORI (Economic Operators Registration and Identification). Additionally, the domainName field can either be a String or a List<String> representing the domain names of the entity.

### Verifier 

#### CredentialVerificationApi
This class sends a request to an OID4VC compliant, credential verification API in order to verify a signed credential. In provenAI's context the WaltID verifier API is used []. The credential verifier api functionality is described in [section].