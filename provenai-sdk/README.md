# ProvenAI SDK

The ProvenAI SDK is a Java-based toolkit designed to facilitate the integration of 9rd-party applications with the ProvenAI ecosystem. It provides developers with the necessary tools to implement features such as:
- Issuing AI Agent ID Verifiable Credentials (VC): Enables the creation of secure and verifiable IDs for AI agents.
- Generating AI Agent ID Verifiable Presentations (VP): Facilitates the verification process of AI agents' identities.
- Requesting Data: Simplifies the process of requesting and accessing data from ProvenAI, ensuring controlled and secure data flow.
- Interacting with ProvenAI Blockchain Smart Contracts: Provides the means to engage with blockchain technology for transparent and immutable record-keeping.
- Generating ISCC codes: Creates a unique ISCC code to track and verify content

This SDK is open-source and comes with detailed guides to help developers use its features. It aims to make it easier for developers to work with ProvenAI by providing straightforward ways to integrate with its services.


## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6.3 or higher

### Installation
To use the ProvenAI SDK in your project, add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>dev.ctrlspace.provenai</groupId>
    <artifactId>provenai-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Blockchain Configuration

#### Generate Java classes

Java classes - to interact with ProveAI Smart Contracts - has already been generated and included in the project. 
To re-generate the java classes from the smart contract ABI you will need

1. Install Solidity Compiler [solc](https://docs.soliditylang.org/en/develop/installing-solidity.html)
```shell
brew update
brew upgrade
brew tap ethereum/ethereum
brew install solidity
```

2. Install [web3j](https://github.com/hyperledger/web3j?tab=readme-ov-file#quickstart) command line tool. 

For Unix:
```shell
curl -L get.web3j.io | sh && source ~/.web3j/source.sh
```

For Windows:
```shell
Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/web3j/web3j-installer/master/installer.ps1'))
```

3. Compile Smart Contract

Smart contracts are handled by the [ProvenAI Blockchain]() module. In this module the ABIs are created by executing the command 
```shell
cd ./provenai-blockchain
forge build
cd ..
```
In the file `provenai-blockchain/out/ProvenAIAgentUsage.sol/ProvenAIAgentUsage.json` you will find the ABI.

You need to update this in the SDK module so that the Java classes can be generated with the latest version.
To extract the ABI you need to run:
```shell
jq '.abi' provenai-blockchain/out/ProvenAIAgentUsage.sol/ProvenAIAgentUsage.json > provenai-sdk/src/main/java/dev/ctrlspace/provenai/blockchain/abis/ProvenAIAgentUsage.abi
```


4. Generate Java classes (see more [here](https://docs.web3j.io/4.8.7/smart_contracts/construction_and_deployment/#solidity-smart-contract-wrappers))
```shell

web3j generate solidity -a <contract>.abi -o <output-dir>/ -p <package-name>
```

e.g. for ProvenAIAgentUsage.sol
```shell
web3j generate solidity -a ./provenai-sdk/src/main/java/dev/ctrlspace/provenai/blockchain/abis/ProvenAIAgentUsage.abi -o ./provenai-sdk/src/main/java -p dev.ctrlspace.provenai.blockchain.model
```


