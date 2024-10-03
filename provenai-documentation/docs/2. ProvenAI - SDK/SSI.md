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

DidIssuer didIssuer = new DidIssuer();
JWKKey jwkKey = JWKKey.generate(KeyType.RSA, JWKKeyMetadata());
DidResult didResult = didIssuer.createDidFromKey(KeyType.RSA, jwkKey);


- **createDidFromAutoKey**
- **createDidFromWeb**

