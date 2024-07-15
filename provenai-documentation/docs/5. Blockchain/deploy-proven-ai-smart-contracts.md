# Deploy Proven AI Smart Contracts

## Introduction

This guide will walk you through deploying Proven AI smart contracts to the EVM (Ethereum compatible) mainnet or testnets.

## Prerequisites

1. Install Foundry CLI
```
curl -L https://foundry.paradigm.xyz | bash
```

2. [Install Ganache (optional)](https://archive.trufflesuite.com/ganache/) or use [anvil](https://book.getfoundry.sh/anvil/)

3. [Install Metamask (optional)](https://metamask.io/)

4. Add your Ganache/Etherium account in to Metamsk


## Deploy

1. Clone the Proven AI smart contracts repository
```
git clone https://github.com/ctrl-space-labs/proven-ai.git
```

2. Change directory to the smart contracts repository
```
cd proven-ai/provenai-blockchain
```

3. Build the smart contracts
```
forge build
```

4. Deploy the smart contracts
```
forge script script/ProvenAIAgentUsageScript.s.sol:ProvenAIAgentUsageScript --rpc-url $RPC_URL --private-key $PRIVATE_KEY --gas-limit 500000 --gas-price 3200000
0000 --broadcast
```

## Useful commands

1. Get the smart contract address and verify the deployment
```
<!-- Call method -->
cast call 0x2cEd717C09C6F79B6314F6A8F36792A67Ba861dA "getDailyUsage(address,uint256)" 0x7f2Ab1BfaEb0A312c89E09e4691000Be9cA14d4a 1720396800000 --rpc-url $AMOY_POLYGON_RPC_URL

<!-- send transaction -->
cast send 0x2cEd717C09C6F79B6314F6A8F36792A67Ba861dA "recordDailyUsage(uint256,bytes32)" 1720396800000 0x5468317320abcdef1234567890abcdef1234567890abcdef1234567890abcdef --rpc-url $RPC_URL --private-key $PRIVATE_KEY --gas-limit 50000 --gas-price 200000000000
```

2. Send ETH to an address
```
cast send <recipient_address> --value <amount_in_wei> --rpc-url <rpc_url> --private-key <private_key>
```

3. Estimate gas
```
cast estimate 0x2cEd717C09C6F79B6314F6A8F36792A67Ba861dA "recordDailyUsage(uint256,bytes32)" 1720396800000 0x5468317320abcdef1234567890abcdef1234567890abcdef1234567890abcdef --rpc-url $AMOY_POLYGON_RPC_URL
```



