# ProvenAI - Frontend

This is the Fronted component of ProvenAI

Sure! Below is an example of how you can update your `README.md` file to include instructions on how to use the different environments in your Next.js application.


# Materialize Next.js Admin Template

## Overview

This project is a Next.js-based admin template with multiple environments (local, development, and production) configured for ease of use. This document will guide you through setting up and running the application in different environments.

## Getting Started

### Prerequisites

Make sure you have the following installed on your machine:

- Node.js (>= 14.x)
- Yarn package manager

### Installation

1. Clone the repository:
   ```bash
   git clone proven ai repository
   
   ```

2. Install the dependencies:
   ```bash
   yarn install
   ```

## Environment Configuration

This project uses environment variables to manage configurations for different environments (local, development, and production). Environment variables are stored in `.env` files.

### Environment Files

Create the following `.env` files in the root of the project:

#### `.env.local`
```env
NEXT_PUBLIC_OIDC_AUTHORITY=********************************
NEXT_PUBLIC_OIDC_CLIENT_ID==********************************
NEXT_PUBLIC_OIDC_REDIRECT_URI==********************************
NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI==********************************
NEXT_PUBLIC_OIDC_SILENT_REDIRECT_URI==********************************
NEXT_PUBLIC_GENDOX_URL==********************************
NEXT_PUBLIC_PROVEN_URL==********************************
NEXT_PUBLIC_VERIFIER_URL==********************************
```

#### `.env.development`
```env
NEXT_PUBLIC_OIDC_AUTHORITY=********************************
NEXT_PUBLIC_OIDC_CLIENT_ID==********************************
NEXT_PUBLIC_OIDC_REDIRECT_URI==********************************
NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI==********************************
NEXT_PUBLIC_OIDC_SILENT_REDIRECT_URI==********************************
NEXT_PUBLIC_GENDOX_URL==********************************
NEXT_PUBLIC_PROVEN_URL==********************************
NEXT_PUBLIC_VERIFIER_URL==********************************
```

#### `.env.production`
```env
NEXT_PUBLIC_OIDC_AUTHORITY=********************************
NEXT_PUBLIC_OIDC_CLIENT_ID==********************************
NEXT_PUBLIC_OIDC_REDIRECT_URI==********************************
NEXT_PUBLIC_OIDC_POST_LOGOUT_REDIRECT_URI==********************************
NEXT_PUBLIC_OIDC_SILENT_REDIRECT_URI==********************************
NEXT_PUBLIC_GENDOX_URL==********************************
NEXT_PUBLIC_PROVEN_URL==********************************
NEXT_PUBLIC_VERIFIER_URL==********************************
```

### Running the Application

#### Local Environment

To run the application in the local environment, use the following command:
```bash
yarn start:local
```
This will start the development server on port 3001 with the local environment variables.

#### Development Environment

To run the application in the development environment, use the following command:
```bash
yarn start:dev
```
This will start the development server on port 3001 with the development environment variables.

#### Production Environment

To build and start the application in the production environment, use the following commands:

Build the application:
```bash
yarn start:prod
```

Start the application:
```bash
yarn start
```
This will start the server on port 3001 with the production environment variables.

### Additional Scripts

- **Linting**: Run ESLint to check for linting errors and automatically fix them.
  ```bash
  yarn lint
  ```

- **Formatting**: Run Prettier to format the code.
  ```bash
  yarn format
  ```

- **Building Icons**: Bundle icons using the custom script.
  ```bash
  yarn build:icons
  ```

## Project Structure

- `src/`: Contains the source code of the application.
- `public/`: Contains public assets.
- `.env*`: Environment variable files.
- `package.json`: Contains the project metadata and scripts.



