import React, { useState, useEffect } from "react";
import { useRouter } from "next/router";
import { useAuth } from "src/hooks/useAuth";

import dataPodsService from "src/provenAI-sdk/dataPodsService";
import agentService from "src/provenAI-sdk/agentService";
import authConfig from "src/configs/auth";
import { sortByField } from "src/utils/orderUtils";


const navigation = () => {
  const auth = useAuth();
  const router = useRouter();
  const { organizationId } = router.query;
  const [navigationItems, setNavigationItems] = useState([]);

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  useEffect(() => {
    const fetchData = async () => {
      if (auth.user && auth.user.provenOrgs) {
        try {
          const activeOrganization = auth.user.provenOrgs.find(
            (org) => org.id === organizationId
          );

          let dataPods = [];
          let agents = [];

          if (activeOrganization) {
            const dataPodsResponse =
              await dataPodsService.getDataPodsByOrganization(
                activeOrganization.id,
                storedToken
              );


            dataPods = dataPodsResponse.data.content.map((dataPod) => {
              return {
                title: dataPod.podUniqueName,
                icon: "mdi:server",
                path: `/provenAI/data-pods-control/?organizationId=${activeOrganization.id}&dataPodId=${dataPod.id}`,
                itemId: dataPod.id,                
              };
            });
            const agentResponse = await agentService.getAgentsByOrganization(
              activeOrganization.id,
              storedToken
            );
            agents = agentResponse.data.content.map((agent) => {
              return {
                title: agent.agentName,                
                icon: "mdi:creation",
                path: `/provenAI/agent-control/?organizationId=${activeOrganization.id}&agentId=${agent.id}`,
                itemId: agent.id,                
              };
            });
          }

          const sortedDataPods = sortByField(dataPods, "title");
          const sortedAgents = sortByField(agents, "title");

            setNavigationItems([
              {
                sectionTitle: "DATA PODS",
                sectionButton: "dataPod"
              },
              ...sortedDataPods,

              
              {
                sectionTitle: "AI AGENTS",
              },
              ...sortedAgents,   
                      
            ]);
          
        } catch (error) {
          console.error("Error fetching data pods:", error);
        }
      }
    }
    
    fetchData();
  }, [auth, organizationId, router]);

  return navigationItems;
};

export default navigation;
