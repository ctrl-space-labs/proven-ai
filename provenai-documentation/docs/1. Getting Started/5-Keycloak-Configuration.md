# Keycloak Configuration

### Step 1: Clone the gendox-core repository
```bash
git clone https://github.com/ctrl-space-labs/gendox-core.git
```

### Step 2: Start keycloak server

#### Keycloak server setup with docker


#### Manual keycloak server setup

Start Keycloak in development mode. From gendox-core
```bash
cd ./gendox-keycloak
```
- Linux/MacOS
```bash
bin/kc.sh start-dev --http-port=8880
```
- Windows
```bash
bin\kc.bat start-dev --http-port=8880
```
You can also configure any other port to run the keycloak server.




### Step 3: Create keycloak admin user
- Go to the Keycloak admin console at `http://localhost:8880`
- Create an administrative user. You need to set the username and password for the admin user.

### Step 4: Create `gendox-idp-dev` realm
Open the Administration Consoler after creating the admin user. There is a default `master` realm created. Click on create realm and enter as name `gendox-idp-dev`.

### Step 5: Fill in admin user information
Switch to `master` real and under the `Users` you will se the admin user. Fill in the email of the user as well as the first and last name of the user.

### Step 6: Configure realm clients
Navigate to `clients` to configure clients settings.


- Create `gendox-pkce-public-client-local` client. Fill in the client information with the following values:

| **Category**         | **Field**                               | **Value**                             |
|----------------------|-----------------------------------------|---------------------------------------|
| **General settings**  | Client ID *                             | `gendox-pkce-public-client-local`      |
|                      | Name                                    | `Local Gendox PKCE client`                    |
|                      | Description                             | `PKCE Public Client for gendox`       |
|                      | Always display in UI                    | `On`                                  |
| **Access settings**   | Root URL                                | `http://localhost:3000`              |
|                      | Home URL                                | `http://localhost:3000/gendox/home`               |
|                      | Valid redirect URIs                     | `http://localhost:3000/*`             |
|                      | Valid post logout redirect URIs         | `http://localhost:3000/login`      |
|                      | Web origins                             | `http://localhost:3000`                    |
|                      | Admin URL                             | `http://localhost:3000`                    |
| **Capability config** | Client authentication                   | `Off`                                 |
|                      | Authorization                           | `Off`                                 |
|                      | Authentication flow                     | `Standard flow`                       |
|                      | Authentication flow                     | `Direct access grants`                |
|                      | Direct access grants                    | `On`                                  |
|                      | Implicit flow                           | `Off`                                 |
|                      | OAuth 2.0 Device Authorization Grant    | `Off`                                 |

We also need to modify the following under advanced settings:
| **Category**            | **Field**                                          | **Value**                       |
|-------------------------|----------------------------------------------------|---------------------------------|
| **Advanced Settings**   | Proof Key for Code Exchange Code Challenge Method  | `your-code-challenge-method`     |


We note here that `3000` is the port for gendox frontend.


- Create `gendox-private-client` client. Fill in the client information with the following values:

| **Category**         | **Field**                               | **Value**                             |
|----------------------|-----------------------------------------|---------------------------------------|
| **General settings**  | Client ID *                             | `gendox-private-client`      |
|                      | Name                                    | `Gendox Private Client`                    |
|                      | Always display in UI                    | `On`                                  |
| **Access settings**  | Valid redirect URIs                     | `http://localhost:8080/*`             |
|                      | Web origins                             | `http://localhost:8080`                    |
| **Capability config** | Client authentication                   | `On`                                 |
|                      | Authorization                           | `Off`                                 |
|                      | Authentication flow                     | `Standard flow`                       |
|                      | Authentication flow                     | `Direct access grants`                |
|                      | Direct access grants                    | `On`                                  |
|                      | Implicit flow                           | `Off`                                 |
|                      | OAuth 2.0 Device Authorization Grant    | `Off`                                 |

We note here that `8080` is the port for gendox backend.



- Create `gendox-private-client` client. Fill in the client information with the following values:

| **Category**         | **Field**                               | **Value**                             |
|----------------------|-----------------------------------------|---------------------------------------|
| **General settings**  | Client ID *                             | `gendox-public-client`      |
|                      | Name                                    | `gendox-public-client`                    |
|                      | Description                             | `Public client to use in the frontend!`       |
|                      | Always display in UI                    | `On`                                  |
| **Access settings**  | Valid redirect URIs                     | `http://localhost:8080/*`             |
|                      | Web origins                             | `http://localhost:8080`                    |
| **Capability config** | Client authentication                   | `Off`                                 |
|                      | Authorization                           | `Off`                                 |
|                      | Authentication flow                     | `Standard flow`                       |
|                      | Authentication flow                     | `Direct access grants`                |
|                      | Authentication flow                     | `Service accounts roles`                |
|                      | Direct access grants                    | `On`                                  |
|                      | Implicit flow                           | `Off`                                 |
|                      | OAuth 2.0 Device Authorization Grant    | `Off`                                 |

We note here that `8080` is the port for gendox backend.
Under the `Service account roles` we need to assign the following roles:
- `manage-users`
- `impersonation`
- `view-users`
- `query-users`

- Create `proven-ai-pkce-client` client. Fill in the client information with the following values:

| **Category**         | **Field**                               | **Value**                             |
|----------------------|-----------------------------------------|---------------------------------------|
| **General settings**  | Client ID *                             | `proven-ai-pkce-client`      |
|                      | Always display in UI                    | `On`                                  |
| **Access settings**  | Valid redirect URIs                     | `/*`             |
|                      | Web origins                             | `/*`                    |
| **Capability config** | Client authentication                   | `Off`                                 |
|                      | Authorization                           | `Off`                                 |
|                      | Authentication flow                     | `Standard flow`                       |
|                      | Authentication flow                     | `Direct access grants`                |
|                      | Direct access grants                    | `On`                                  |
|                      | Implicit flow                           | `Off`                                 |
|                      | OAuth 2.0 Device Authorization Grant    | `Off`                                 |

We also need to modify the following under advanced settings:
| **Category**            | **Field**                                          | **Value**                       |
|-------------------------|----------------------------------------------------|---------------------------------|
| **Advanced Settings**   | Proof Key for Code Exchange Code Challenge Method  | `your-code-challenge-method`     |


- Create `proven-ai-private-client` client. Fill in the client information with the following values:

| **Category**         | **Field**                               | **Value**                             |
|----------------------|-----------------------------------------|---------------------------------------|
| **General settings**  | Client ID *                             | `proven-ai-private-client`      |
|                      | Name                                    | `ProvenAI Private Client`                    |
|                      | Always display in UI                    | `On`                                  |
| **Access settings**  | Valid redirect URIs                     | `/*`             |
|                      | Web origins                             | `/*`                    |
| **Capability config** | Client authentication                   | `On`                                 |
|                      | Authorization                           | `Off`                                 |
|                      | Authentication flow                     | `Standard flow`                       |
|                      | Authentication flow                     | `Direct access grants`                |
|                      | Authentication flow                     | `Service accounts roles`                |
|                      | Direct access grants                    | `On`                                  |
|                      | Implicit flow                           | `Off`                                 |
|                      | OAuth 2.0 Device Authorization Grant    | `Off`                                 |

Then create the following role under the `Roles` section:
| **Field**         | **Value**              |
|--------------------|------------------------|
| Role name *        | `uma_protection`       |
| Description         |      |


Under the `Service account roles` we need to assign the following roles:
- `manage-users`
- `impersonation`
- `view-users`
- `query-users`
- `uma_protection`

- Create `proven-pkce-public-client-local` client. Fill in the client information with the following values:

| **Category**         | **Field**                               | **Value**                             |
|----------------------|-----------------------------------------|---------------------------------------|
| **General settings**  | Client ID *                             | `proven-pkce-public-client-local`      |
|                      | Name                                    | `Local Gendox PKCE client`                    |
|                      | Description                             | `PKCE Public Client for gendox`       |
|                      | Always display in UI                    | `On`                                  |
| **Access settings**   | Root URL                                | `http://localhost:3001`              |
|                      | Home URL                               | `http://localhost:3001/gendox/home`         |
|                      | Valid redirect URIs                     | `http://localhost:3001/*`             |
|                      | Valid post logout redirect URIs         | `http://localhost:3001/login`      |
|                      | Web origins                             | `http://localhost:3001`                    |
|                      | Admin URL                             | `http://localhost:3001`                    |
| **Capability config** | Client authentication                   | `Off`                                 |
|                      | Authorization                           | `Off`                                 |
|                      | Authentication flow                     | `Standard flow`                       |
|                      | Authentication flow                     | `Direct access grants`                |
|                      | Direct access grants                    | `On`                                  |
|                      | Implicit flow                           | `Off`                                 |
|                      | OAuth 2.0 Device Authorization Grant    | `Off`                                 |

We note here that `3001` is the port for provenAI frontend.
We also need to modify the following under advanced settings:
| **Category**            | **Field**                                          | **Value**                       |
|-------------------------|----------------------------------------------------|---------------------------------|
| **Advanced Settings**   | Proof Key for Code Exchange Code Challenge Method  | `your-code-challenge-method`     |

### Step 7: Create role users
In the section **Realm roles** create the role `user`

| **Field**                   | **Value**                             |
|-----------------------------|---------------------------------------|
| Name *                      | `user`                           |
| Description                  | `This is a role for users`                    |


### Step 8: Modify Realm Settings
Under the section **Realm settings** configure the following settings:
- **General**
Enable `Unmanaged Attributes`

- **Login**

| **Category**               | **Field**               | **Value**       |
|----------------------------|-------------------------|-----------------|
| **Login screen customization** | User registration        | `On`            |
|                            | Forgot password          | `On`            |
|                            | Remember me              | `Off`           |
| **Email settings**          | Email as username        | `Off`           |
|                            | Login with email         | `On`            |
|                            | Duplicate emails         | `Off`           |
|                            | Verify email             | `Off`            |
| **User info settings**      | Edit username            | `On`            |


- **Email**
If you want to enable email verification and other functionalities that require an email address you can configure the email settings below:

| **Category**                     | **Field**                | **Value**            |
|-----------------------------------|--------------------------|----------------------|
| **Template**                      | From *                   | `your-email-address` |
| **Connection & Authentication**   | Host *                   | `your-smtp-host`     |
|                                   | Port                     | `your-smtp-port`     |
|                                   | Encryption               | `your-encryption`    |
|                                   | Enable SSL               | `Off`             |
|                                   | Enable StartTLS          | `On`             |
| **Authentication**                | Enabled                  | `On`             |

- **Theme**
Select as login theme `gendox`.

- **Keys**
Switch to Add providers. Add `hmac-generated`:

| **Category**      | **Field**                | **Value**                   |
|-------------------|--------------------------|-----------------------------|
| **Edit provider** | Name *                   | `hmac-generated`         |
|                   | Priority                 | `100`        |
|                   | Priority for the provider| `your-priority-description`  |
|                   | Algorithm                | `HS256`             |

- **Sessions**

| **Category**             | **Field**                      | **Value**                |
|--------------------------|--------------------------------|--------------------------|
| **SSO Session Settings**  | SSO Session Idle               | `12 Hours`  |
|                          | SSO Session Max                | `1 Day`   |

- **Tokens**

| **Category**       | **Field**                           | **Value**                     |
|--------------------|-------------------------------------|-------------------------------|
| **Access Tokens**   | Access Token Lifespan              | `1 Hour`          |
|                    | Access Token Lifespan For Implicit Flow | `12 Hours`  |
|                    | Client Login Timeout               | `your-login-timeout`           |

### Step 9: Authentication Settings
 We need to create a `Copy of registration` flow on flows:

| **Category**      | **Field**            | **Value**               |
|-------------------|----------------------|-------------------------|
| **Create Flow**    | Name *               | `Copy of registration`        |

Next we need to configure the flows.
- Click on registration flow and duplicate it.
- On the Copy of registration set `Terms&Condiitons` to `Required`.
- Under Action select `Bind flow`. Choose as binding type as `Registration flow`.

Switch to `Keycloak master` realm and repeat this process.


Next switch to `Required actions`. You will need to change the following values:
| **Field**                   | **Value**                             |
|-----------------------------|---------------------------------------|
| Terms and Conditions        | `On`                           |
| Verify                      | `Off`                        |

### Step 10: Keycloak environment variables and application properties
AFter configuring keycloak you will need to update the environment variables:
| Environment Variable      | Example value                                              |
|--------------------------|------------------------------------------------------------|
| `KEYCLOAK_CLIENT_ID`      | `gendox-private-client`                                    |
| `KEYCLOAK_CLIENT_SECRET`  | `**********`                                               |

The `KEYCLOAK_CLIENT_SECRET` is found on the `credentials` tab on the `gendox-private-client` client.

- Set up keycloak configuration on application properties.
First on the [Security Configuration](../Getting%20Started/Configuration#security-configuration-oauth2-and-jwt)
    - `issuer-uri`: `http://localhost:8880/realms/gendox-idp-dev`
    - `jwk-set-uri`: `http://localhost:8880/realms/gendox-idp-dev/protocol/openid-connect/certs`
Thenk on the [Keycloak Configuration](../Getting%20Started/Configuration#keycloak-configuration)
    - `base-url`: `http://localhost:8880`
    - `token-uri`: `http://localhost:8880/realms/gendox-idp-dev/protocol/openid-connect/token`
    - `realm`: `gendox-idp-dev`

### Step 10: Create `proven-ai-private-client` and users
For the interaction between the Gendox and ProvenAI services we must create a user in the database for `proven-ai-private-client` and, following the steps below:
- 

   