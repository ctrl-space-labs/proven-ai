## Create DataPod Verifiable Credential
This endpoint is used to creates a Data pod Ownership Credential for data pod data. To create the verifiable credential of this specific type we must set the  credentialConfigurationId to "VerifiableDataOwnershipCredential_jwt_vc_json", when sending the request with [].

### Prerequites
Issuer did and key in JWK format. In this context, provenAI is the issuer. These values are passed as environment variables.
 Environment Variable | Description                                    | Example Value                               |
|----------------------|------------------------------------------------|---------------------------------------------|
| `ISSUER_DID`         | The Decentralized Identifier of the issuer     |`did:example:123456789abcdefghi`            |
| `ISSUER_PRIVATE_JWK`     | The JSON Web Key of the issuer in JWK format   | `{"kty":"EC","crv":"P-256","x":"...","y":"..."}` |


### **HTTP Method:**
`POST`

### **URL:**
`/data-pods/{dataPodId}/credential-offer`

### **Authorization:**
- **Required Authority**: `OP_EDIT_PROVEN_AI_DATAPOD`
- **Condition**: This endpoint is protected by a `@PreAuthorize` check, ensuring that only users with the authority `OP_EDIT_PROVEN_AI_DATAPOD` can access it. Additionally, the `dataPodId` is extracted using `getRequestedDataPodIdFromPathVariable` for validation.

### **Path Parameters:**
- `dataPodId` (String):  
  The unique identifier of the DataPod for which the Verifiable Credential offer is being created. 

### **Request Body:**
- No request body is needed. The `dataPodId` is passed as a path variable.

### **Response:**

The response contains the `VCOfferDTO` object with the following fields:

- **`id` (String)**:  
  The unique identifier of the DataPod.

- **`credentialOfferUrl` (String)**:  
  The URL to the Verifiable Credential offer generated for the DataPod.

- **`credentialJwt` (Object)**:  
  The signed JWT (JavaScript Web Token) representing the Verifiable Credential for the DataPod.


Also CRUD operations are available for the `Data Pod` entity. Full API reference [here](https://dev.proven-ai.ctrlspace.dev/proven-ai/api/v1/swagger-ui/index.html#/Data%20Pods).
