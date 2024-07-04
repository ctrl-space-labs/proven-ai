// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/Ownable.sol";

contract ProvenAIAgentUsage is Ownable {
    // Struct to store the daily usage data with the Merkle root hash
    struct DailyUsage {
        bytes32 rootHash; // The root hash of the Merkle tree for the day's data
    }

    // Struct to encapsulate daily usage mapping
    struct AgentUsage {
        mapping(uint256 => DailyUsage) dailyUsage; // Mapping from date to DailyUsage struct
    }

    // Mapping to store daily usage data for each agent
    mapping(address => AgentUsage) private agentUsageData;

    // Event to be emitted when daily usage data is updated
    event DailyUsageDataUpdated(address indexed agent, uint256 indexed date, bytes32 rootHash);

    /**
     * @dev Constructor that initializes the contract.
     */
    constructor() Ownable(msg.sender) {}


    /**
     * @dev Record daily usage data for the calling agent.
     * @param date The date for which the data is recorded (typically as a Unix timestamp).
     * @param rootHash The root hash of the Merkle tree containing the usage data.
     */
    function recordDailyUsage(uint256 date, bytes32 rootHash) public {
        require(rootHash != bytes32(0), "Invalid root hash");

        // Check if data for this date already exists
        DailyUsage storage existingUsage = agentUsageData[msg.sender].dailyUsage[date];
        require(existingUsage.rootHash == bytes32(0), "Data for this date already recorded");

        // Store the new usage data
        agentUsageData[msg.sender].dailyUsage[date] = DailyUsage({ rootHash: rootHash });

        // Emit an event to log the update
        emit DailyUsageDataUpdated(msg.sender, date, rootHash);
    }

    /**
     * @dev Get the daily usage data for a specific agent and date.
     * @param agent The address of the agent.
     * @param date The date for which the data is requested (typically as a Unix timestamp).
     * @return The root hash of the Merkle tree for the specified agent and date.
     */
    function getDailyUsage(address agent, uint256 date) public view returns (bytes32) {
        return agentUsageData[agent].dailyUsage[date].rootHash;
    }
}