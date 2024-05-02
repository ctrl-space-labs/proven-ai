// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/Ownable.sol";

contract ProvenAIAgentUsage is Ownable {
    struct DailyUsageRoot {
        bytes32 rootHash;
        uint256 tokenSum;
    }

    mapping(address => mapping(uint256 => DailyUsageRoot)) private usageData;

    event DailyUsageDataUpdated(address indexed agent, uint256 date, bytes32 rootHash, uint256 tokenSum);

    /**
        * @dev Record daily usage data for an agent.
        * @param date The date for which the data is recorded.
        * @param rootHash The root hash of the Merkle tree containing the usage data.
        * @param tokenSum The total number of tokens spent by the agent on this date.
    */

    function recordDailyUsage(uint256 date, bytes32 rootHash, uint256 tokenSum) public onlyOwner {
        require(rootHash != bytes32(0), "Invalid root hash");
        require(tokenSum > 0, "Token sum must be positive");

        DailyUsageRoot storage existingRoot = usageData[msg.sender][date];
        require(existingRoot.rootHash == bytes32(0), "Data for this date already recorded");

        DailyUsageRoot memory newUsageRoot = DailyUsageRoot({
            rootHash: rootHash,
            tokenSum: tokenSum
        });

        usageData[msg.sender][date] = newUsageRoot;
        emit DailyUsageDataUpdated(msg.sender, date, rootHash, tokenSum);
    }

    /**
        * @dev Get the daily usage data for an agent.
        * @param agent The address of the agent.
        * @param date The date for which the data is requested.
        * @return The daily usage data for the agent on the specified date.
    */
    function getDailyUsage(address agent, uint256 date) public view returns (DailyUsageRoot memory) {
        return usageData[agent][date];
    }
}