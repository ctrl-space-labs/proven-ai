---
sidebar_position: 1
---

## Introduction
ProvenAI is a platform that enables you to create, manage, and verify self-sovereign identities (SSI) for your users.
To Issue and verify credentials, you need to set up Issuers and Verifiers. This guide will walk you through the process of setting up Issuers and Verifiers on ProvenAI.

The Walt.id platform is a self-sovereign identity (SSI) platform that enables you to create, manage, and verify self-sovereign identities for your users. To issue and verify credentials, you need to set up Issuers and Verifiers. 
This guide will walk you through the process of setting up Issuers and Verifiers on Walt.id.


## Setup WaltId Services 

## Pre-requisites
Install Docker on your machine.
## Steps
### 1. Cloning the walt.id Indentity Repository

```
git clone https://github.com/walt-id/waltid-identity.git && cd waltid-identity
```

### 2. Setup Services
With the wallt.id identity package we can now run all APIs with docker:

```
cd docker-compose && docker-compose up
```
## Port Mapping
- Issuer API: http://localhost:7002
- Verifier API: http://localhost:7003
- Wallet API: http://localhost:7001
- Web Wallet (Frontend): http://localhost:7101

### Credential Issuance API
### Route: POST `/openid4vc/jwt/issue`

### Description
This API enables the issuance of a credential utilizing the OID4CI protocol to issue a signed credential. It generates a credential offer url that can be imported to any OID compliant wallet to receive the credential.

### RequestBody
- **VerifiableCredential**: The verifiable credential to be issued in json format.
  - Type: Json Node

### Response
The credential offer URL of the credential to be issued.
#### Example value: 
openid-credential-offer://localhost/?credential_offer=%7B%22credential_issuer%22%3A%22http%3A%2F%2Flocalhost%3A8000%22%2C%22credentials%22%3A%5B%22VerifiableId%22%5D%2C%22grants%22%3A%7B%22authorization_code%22%3A%7B%22issuer_state%22%3A%22501414a4-c461-43f0-84b2-c628730c7c02%22%7D%7D%7D

### Credential Verification API
### Route: POST `/openid4vc/verify`

### Description
This API initialized an OIDC presentation session, given a presentation definition. The URL returned can be rendered as QR code for the holder.

### RequestBody
- **Presentation Definition**: The verifiable presentation in json format.  It describes the presentation requirement for this verification session and contains the VP, global VC and specific VC, that will be checked for the credntial to be verified.
  - Type: Json Node

### HTTP Headers

#### `authorizeBaseUrl`

- **Description:** Base URL of the wallet authorize endpoint used in OIDC sessions (defaults to `openid4vp://authorize`).
- **Value:** `openid4vp://authorize`

#### `responseMode`

- **Description:** Response mode for the OIDC presentation session.
- **Value:** `direct_post` indicating the response should be returned directly via HTTP POST.

#### `successRedirectUri`

- **Description:** Redirect URI to return upon successful verification.

#### `errorRedirectUri`

- **Description:** Redirect URI to return when a policy fails.

The error and success URIS are set in the `SSIConstants` class. The "$id" will be replaced with the session id.

### Response
The verification URL containing the presentation definition. We can provide the URL or the QR code rendered in the wallet to verify Verifiable Credentials against the presentation definition provided. When the verification is successful we will be redirected to the successRedirectUri. If the verification fails, meaning none of the Verifiable Credentials in our wallet fullfill the presentation definition provided, we will be redirected to the errorRedirectUri.
#### Example value: 
openid4vp://authorize?response_type=vp_token&client_id=&response_mode=direct_post&state=M4d9Xz2e073Z&presentation_definition_uri=http%3A%2F%2Fverifier-api%3A7003%2Fopenid4vc%2Fpd%2FM4d9Xz2e073Z&client_id_scheme=redirect_uri&response_uri=http%3A%2F%2Fverifier-api%3A7003%2Fopenid4vc%2Fverify%2FM4d9Xz2e073Z