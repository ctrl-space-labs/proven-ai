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
forge script script/Counter.s.sol:CounterScript --rpc-url $RPC_URL --private-key $PRIVATE_KEY --broadcast
```

## Useful commands

1. Get the smart contract address and verify the deployment
```
<!-- Call method -->
cast call 0x703fAD8Fccb141ceCb64c06f8CC284A93e720E97 "getNumber()" --rpc-url $RPC_URL

<!-- send transaction -->
cast send $SMART_CONTRACT_ADDRESS "setNumber(uint256)" 17 --rpc-url $RPC_URL --private-key $PRIVATE_KEY
```

2. Send ETH to an address
```
cast send <recipient_address> --value <amount_in_wei> --rpc-url <rpc_url> --private-key <private_key>
```

3. Estimate gas
```
cast estimate --to $SMART_CONTRACT_ADDRESS --data $(cast calldata "setNumber(uint256)" 17) --rpc-url $RPC_URL
```



