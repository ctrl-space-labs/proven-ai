import {useEffect, useState} from "react";
import {useAuth} from "src/authentication/useAuth";
import {useRouter} from "next/router";
import {sortByField} from "src/utils/orderUtils";
import dataPodsService from "src/provenAI-sdk/dataPodsService";
import {localStorageConstants} from "src/utils/generalConstants";
import agentService from "../../provenAI-sdk/agentService";

const navigation = () => {
  const auth = useAuth();
  const router = useRouter();
  const { organizationId, projectId } = router.query;
  const [navigationItems, setNavigationItems] = useState([]);

  const token = window.localStorage.getItem(
    localStorageConstants.accessTokenKey
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
                token
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
              token
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
  }, [auth, organizationId, router, projectId]);

  return navigationItems;
}

export default navigation
