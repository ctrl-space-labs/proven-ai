## ProvenAI Issuer
The `ProvenAIIssuer` class is designed to generate and sign Verifiable Credentials (VCs) for ProvenAI entities. It provides methods to convert VCs to the W3CVC standard format and to sign these credentials using the issuer's key.
Supported Entities:
- AI Agent
- Permission of Use
- Data Ownership Credential

#### Generate unsigned Verifiable Credentials Method
Generates a W3CVC credential based on a credential subject of a specific schema. It converts the specific credential subjects to adhere to the W3CVC standard in order to produce valid verifiable credentials.

**Signature:** `generateUnsignedVC(VerifiableCredential&lt;? extends CredentialSubject&gt; vc)`
**Parameters:** A verifiable credential subject object `VerifiableCredential&lt;? extends CredentialSubject&gt; vc`.
**Return Type:** A W3CVC verifiable credential with the information provided from the credential subject.
**Exceptions**: JSONException, JsonProcessingException

#### Generate signed Verifiable Credentials Method
Generates a signed W3CVC credential object in JWT format. It signs the VC using the walt.id issuer SDK method `signJws`

**Signature:** `generateSignedVCJwt(W3CVC w3cVC, Key issuerKey, String issuerDid, String subjectDid)`
**Parameters:**
  - `W3CVC`: W3CVC type verifiable credential.
  - `issuerKey`: Key walt.id type. The issuer is the entity that signs the verifiable credential.
  - `issuerDid`: String issuer DID.
  - `subjectDid`: String subject DID. The subject is the holder of the verifiable credential.
**Return Type:** Object signed verifiable credential in JWT format.
