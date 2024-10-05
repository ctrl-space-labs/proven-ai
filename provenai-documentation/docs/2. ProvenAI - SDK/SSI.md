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
        JWKKey issuerKey = KeyCreation.generateKey(KeyType.RSA, 2048);
        DidResult issuerDidResult = didIssuer.createDidFromKey(KeyType.RSA, issuerKey);
        String issuerDid = issuerDidResult.getDid();  // Get the DID of the issuer

        // Generate a JWK key for the subject
        JWKKey subjectKey = KeyCreation.generateKey(KeyType.RSA, 2048);
        DidResult subjectDidResult = didIssuer.createDidFromKey(KeyType.RSA, subjectKey);
        String subjectDid = subjectDidResult.getDid();  // Get the DID of the subject

        // Output the generated DIDs
        System.out.println("Issuer DID: " + issuerDid);
        System.out.println("Subject DID: " + subjectDid);

        // Initialize the verifiable credential builder
        VerifiableCredentialBuilder vcBuilder = new VerifiableCredentialBuilder();

        // Add context (default context: "https://www.w3.org/2018/credentials/v1")
        vcBuilder.addContext("https://www.w3.org/2018/credentials/v1");

        // Add types to the verifiable credential
        vcBuilder.addType("VerifiableCredential");
        vcBuilder.addType("EBSILegalEntityVerifiableID");

        // Set the credential ID
        vcBuilder.setCredentialId("urn:uuid:12345678-abcd-1234-abcd-1234567890ab");

        // Set the issuer's DID
        vcBuilder.setIssuerDid(issuerDid);

        // Set the validity period
        vcBuilder.validFromNow();  // Valid from now
        vcBuilder.validFor(Duration.ofDays(365));  // Valid for 1 year

        // Define the credential subject (example data)
        JsonObject subjectData = new JsonObject(Map.of(
            "id", new JsonPrimitive(subjectDid),  // Using the generated subject DID
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