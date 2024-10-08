## Audit Permission of Use - Blockchain Registration

This endpoint audits the registration of an organization's permission-of-use data onto the blockchain.

### **HTTP Method:**
`POST`

### **URL:**
`/permission-of-use-analytics/audit/blockchain-registration`

### **Authorization:**
- Authentication is required, but no specific role or authority is mentioned in the provided code.

### **Request Parameters:**

- **`organizationDid` (String, Required)**:  
  The Decentralized Identifier (DID) of the organization whose permission-of-use is being audited for blockchain registration.

- **`date` (Instant, Required)**:  
  The date of the permission-of-use audit, represented as an `Instant`.

You can see the API reference for audit permission of use [here](https://dev.proven-ai.ctrlspace.dev/proven-ai/api/v1/swagger-ui/index.html#/Permission%20of%20Use%20Analytics).
