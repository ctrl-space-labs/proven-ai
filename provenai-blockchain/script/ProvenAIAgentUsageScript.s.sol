// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.13;

import {Script} from "forge-std/Script.sol";
import {ProvenAIAgentUsage} from "../src/ProvenAIAgentUsage.sol";

contract ProvenAIAgentUsageScript is Script {
    ProvenAIAgentUsage public provenAIAgentUsage;

    function setUp() public {}

    function run() public {
        vm.startBroadcast();

        provenAIAgentUsage = new ProvenAIAgentUsage();

        vm.stopBroadcast();
    }
}