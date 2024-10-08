---
sidebar_position: 1
---

# Installation

ProvenAI consists of a set of microservices that can be deployed on your own infrastructure. The installation process is straightforward and can be done in a few steps.

## Prerequisites

Before you start the installation process, make sure you have the following prerequisites:
- Docker
- Docker Compose


## Installation

The installation process is simple and can be done in a few steps. It requires just running docker compose 
with some extra steps to generate access keys for the services to interact with each other.

### Step 1: Clone the repository
```bash
git clone https://github.com/ctrl-space-labs/proven-ai.git
```

### Step 2: Set up environment variables

In `./proven-ai/docker-compose/.env`, and set up the environment variables according to [Environment Variables](../Getting%20Started/Environment-Variables) documentation.
```shell
```

### Step 3: Run docker compose
```bash
cd ./proven-ai/docker-compose
docker-compose up
```

### Step 4: Get Keycloak client keys

ProvenAI uses Keycloak for authentication. You need to get the client keys for the services to interact with Keycloak.
1. Go to the Keycloak admin console at `http://localhost:8443`
```shell
```

## Local Development

To run the applications independently for development purposes, you need to install:
- Java 21
- Maven
- Node.js
- NPM or Yarn

Also you need to clone the provenAI repository:

```bash
git clone https://github.com/ctrl-space-labs/proven-ai.git
```

### Running the ProvenAI Backend

ProvenAI backend is a Spring boot application. Once the environment variables are set up (see [here](../Getting%20Started/Environment-Variables)).

#### Step 1: Build and run provenAI SDK docker compose.
To run the backend you also need to run the provenAI SDK docker-compose. To set up the provenAI docker:
```bash
cd ./provenai-sdk/provenai-sdk-docker-compose
docker-compose build
docker-compose up
```
#### Step 2: Run the provenAI Backend.
After the docker-compose is set up you can run the provenAI backend:

```bash
cd ./provenai-backend
mvn clean install
mvn spring-boot:run
```

### Running the ProvenAI Frontend

ProvenAI frontend is a React/Next.js application. No Next.js Server-Side-Rendering features are used. Once the environment variables are set up (see [here](../Getting%20Started/Environment-Variables)),
you can run the frontend using the following command:
```bash
cd ./proven-ai/provenai-frontend
npm install
yarn dev
```
