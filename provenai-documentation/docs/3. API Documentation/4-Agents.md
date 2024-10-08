## Issue Agent Verifiable Credential
Issue verifiable credential for an agent using the W3C standard. The API provides the signe agent Verifiable credential in JWT format. This is created from the provenAI SDK using the walt.id SDK for credential issuance. Also it gnerates an OID4VC offer URL that can be accepted by any OID compliant wallet to receive credential, using the walt.id Credential Issuance API from the provenAI SDK.

### **HTTP Method:**
`POST`

### **URL:**
`/agents/{agentIdd}/credential-offer`

 - **Summary:** Get the signed jwt format of the agent VC and its offer url.

### Prerequites
Issuer did and key in JWK format. In this context, provenAI is the issuer. These values are passed as environment variables.
 Environment Variable | Description                                    | Example Value                               |
|----------------------|------------------------------------------------|---------------------------------------------|
| `ISSUER_DID`         | The Decentralized Identifier of the issuer     |`did:example:123456789abcdefghi`            |
| `ISSUER_PRIVATE_JWK`     | The JSON Web Key of the issuer in JWK format   | `{"kty":"EC","crv":"P-256","x":"...","y":"..."}` |

### Path Variable
- `id`: String agent ID.
### Response:
- `AgentIdCredential`: Java class with fields:
   - `agentId`: String agent ID.
   - `credentialOfferUrl`: String credential offer url.
   - `credentialJwt`: Object signed credential jwt.


## Authorize Agent in ProvenAI
Authorizes an agent in the ProvenAI ecosystem, and returns an authorization token. The agent needs to provide a Verifiable Agent ID Presentation and once the presentation is verified, an authorization token is provided to the agent. To verify the valididty of the VP, the walt.id verifier SDK is used. The VP is chcked against the following policies:
- HolderBindingPolicy: ies that issuer of the Verifiable Presentation (presenter) is also the subject of all Verifiable Credentials contained within.
- JwtSignaturePolicy:Verifies the signature of the W3C JWT-VC
- NotBeforeDatePolicy:erifies that the credentials not-before date (for JWT: nbf, if unavailable: iat - 1 min) is correctly exceeded.
- ExpirationDatePolicy:that the credentials expiration date (exp for JWTs) has not been exceeded.
More information about the supported policies provided in[].

### **HTTP Method:**
`POST`

### **URL:**
`/agents/token`

- **Summary:** Authorizes an agent and returns an authorization token.

### Prerequites
- The signed Agent Verifiable Presentation ID JWT.

### Request Parameters
- `grant_type`: String grant type to be used. Must be 'vp_token'. 
- `scope`: String scope of the request. Must be set to 'openid email'.
- `vp_token`: String signed agent VP ID in JWT format.
### Response:
- `AccessTokenResponse`: Access token response from keycloak.

Also CRUD operations are available for the `Agent` entity. Full API reference [here](https://dev.proven-ai.ctrlspace.dev/proven-ai/api/v1/swagger-ui/index.html#/Agents).
