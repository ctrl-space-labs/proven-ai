'## ProvenAI Verifier
The `ProvenAIVerifier` class is designed to verify signed Verifiable Credentials (VCs) for ProvenAI entities against a set of policies. It provides a method to verify a signed Verifiable presentation jwt, using the `PresentationVerifier` class.

#### Verify Verifiable Presentation Method
Verifies a Verifiable Presentation against a set of policies. The policies can be:
- `vpPolicies`: These policies apply to the whole presentation.
- `globalVcPolicies`: These policies apply to all verifiable credentials in the verifiable presentation.
- `specificCredentialPolicies`: These policies apply to specific credentials.

**Signature:** `Boolean verifyVPJwt(String vpJwt)`
**Parameters:** A signed verifiable presentation `vpJwt`.
**Return Type:** Boolean value.
**Exceptions**: InterruptedException, ExecutionException

This method uses the method verifyPresentationBlocking of the `PresentationVerifier` class as presented in [].