# Keycloak Configuration

### Step 1: Clone the gendox-core repository
```bash
git clone https://github.com/ctrl-space-labs/gendox-core.git
```

### Step 2: Start keycloak server

#### Keycloak server setup with docker
All provenAI services can be set up with docker installation by following the instructions provided [here](../Getting%20Started/Installation#installation-1). When the docker-compose for all the services is built and running you can access the keycloak admin console at `http://localhost:8880` or `https://localhost:8443`. You will need to log in with the username and password you created  on [step] or by following the script instructions on step[].

### Step 3: Setup keycloak clients and realm
A script has been developed to create the `gendox-idp-dev` realm and configure all the necessary clients for the provenAI ecosystem. The `gendox-local-init.sh` is responsible for:
- Logging into Keycloak with admin credentials.
- Creating the `gendox-idp-dev` realm.
- Creating PKCE and private clients.
- Assigning roles to private clients' service accounts.
It also executes the `kcadm.sh` that is responsible for:
- Setting the correct java executable.
- Executing the Keycloak Admin CLI.

#### Prerequisites:
- Linux/MacOS or Windows with WSL
- `java`
- `jq` (for JSON parsing)
- If you are running it with windows and WSL `dos2unix` might be needed.

- **Access `conf` folder of `gendox-keycloak`**
```bash
cd  ../gendox-core/gendox-keycloak/conf
```
- **If you execute the command on WSL with Windows**
Skip this step if you are using Linux/MacOs. We use this package to convert text files with DOS (Windows) line endings (e.g. \r\n) into Unix/Linux line endings.

```bash
dos2unix ../bin/kcadm.sh
dos2unix ./gendox-local-init.sh
```
- **Make the shell files executable**

```bash
chmod +x ../bin/kcadm.sh
chmod +x ./gendox-local-init.sh
```

- **Execute the shell files**

```bash
bash ../bin/kcadm.sh
bash ./gendox-local-init.sh
```
By executing this script you can skip steps 3-6 of the keycloak configuration which speeds up the setup process.

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
| **Advanced Settings**   | Proof Key for Code Exchange Code Challenge Method  | `H256`     |


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
| **Advanced Settings**   | Proof Key for Code Exchange Code Challenge Method  | `H256`     |


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
| **Advanced Settings**   | Proof Key for Code Exchange Code Challenge Method  | `H256`     |

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


### Step 10: Create `proven-ai-private-client` and users
For the interaction between the Gendox and ProvenAI services we must create a user in the database for `proven-ai-private-client` and, following the steps below:


```bash
curl -X GET "http://localhost:8080/gendox/api/v1/profile" \
     -H "Authorization: proven-ai-private-client token"\
```


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