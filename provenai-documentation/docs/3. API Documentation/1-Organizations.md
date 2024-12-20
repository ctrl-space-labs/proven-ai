## Verify Organization VP
Verify an organization verifiable presentation against selected policies. This method creates a verification url after the policies are provided. The user then can copy the url or its can the rendered QR code from the wallet where the organization VP to be verified is stored. This API utilises the walt.id Credential Verification API to provided the verification link. 

### **HTTP Method:**
`POST`

### **URL:**
`/organizations/verify-vp`

 - **Summary:** Get a verification url in order to verify an organization VP stored on your wallet.

### Request Body
- `vpRequest`: Json containing the policies that will be checked in order to verify the organization VP.
### Response:
- `CredentialVerificationDTO`: Java class containing the credentialVerificationUrl.

Also CRUD operations are available for the `Organization` entity. Full API reference [here](https://dev.proven-ai.ctrlspace.dev/proven-ai/api/v1/swagger-ui/index.html#/Registered%20Organizations).
