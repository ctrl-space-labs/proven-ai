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

After generating a key we can issue the DID geenrated from the key. The method `createdDidFromKey()` from `DidIssuer`. It creates a DID from the generated key (`jwkKey`) and the provided `KeyType`. The object returned is a `DidResult`. The `DidKeyCreateOptions` object must be initialized, tahat defines the parameters for the DID created. The necessary inputs are the `keyType` and the boolean `useJwkJcsPub`. If the `useJwkJcsPub` flag is set to true, it applies the EBSI-specific jwk_jcs-pub encoding during the resolution process. To generate the DID, the method `registerByKey` is used from `DidService` The inputs needed is the generation method, dependent on the key provided, the key JWK, the `DidKeyCreateOptions` and a continuation object for kotlin coroutines. In the contect of provenAI the method is se to `"key"`.


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

```java
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;

public class Main {
    public static void main(String[] args) {
        DidIssuer didIssuer = new DidIssuer();
        JWKKey jwkKey = JWKKey.generate(KeyType.RSA, JWKKeyMetadata());
        DidResult didResult = didIssuer.resolveKeyDidToKey(KeyType.RSA, true, jwkKey);
        
        System.out.println("Resolved Key DID: " + didResult.getDid());
    }
}
```