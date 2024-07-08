// ** MUI Imports
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";

// ** Custom Components
import CustomChip from "src/@core/components/mui/chip";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";

const ProvidedByProcessorAgent = () => {
  const [agents, setAgents] = useState([]);
  const [totalTokensProvided, setTotalTokensProvided] = useState(0);
  const [graphData, setGraphData] = useState([]);

  // import permissionOfUseAnalytics redux state here
  const permissionOfUseAnalytics = useSelector(
    (state) => state.permissionOfUseAnalytics
  );
  const userAgents = useSelector(
    (state) => state.userDataForAnalytics.userData.agentsByOrgId.content
  );

  useEffect(() => {
    console.log("graph data updated! ", permissionOfUseAnalytics.graphData);

    if (!permissionOfUseAnalytics.graphData) {
      return;
    }

    if (userAgents) {
      const myAgents = userAgents
        .filter((userAgent) =>
          Object.keys(
            permissionOfUseAnalytics.graphData
              .providedDataTokensByProcessorAgent
          ).includes(userAgent.id)
        )
        .map((userAgent) => ({
          ...userAgent,
          totalSumTokens:
            permissionOfUseAnalytics.graphData
              .providedDataTokensByProcessorAgent[userAgent.id]
              ?.totalSumTokens || 0,
        }));
      setAgents(myAgents);

      // Calculate total tokens consumed
      const totalTokens = myAgents.reduce(
        (sum, agent) => sum + agent.totalSumTokens,
        0
      );
      setTotalTokensProvided(totalTokens);

      const transformedData = myAgents.map((agent) => ({
        imgWidth: 44,
        imgHeight: 44,
        chipText: `${agent.totalSumTokens}`,
        title: agent.agentName,
        imgAlt: "image-alt",
        // subtitle: 'some-subtitle',
        src: "/images/avatars/2.png",
      }));

      setGraphData(transformedData);
    }
  }, [permissionOfUseAnalytics.graphData, userAgents, useSelector]);

  return (
    <Card>
      <CardHeader
        title="Your Agents Provided Data"
        subheader={`Total ${totalTokensProvided} Tokens Provided to other agents!`}
       
      />
      <CardContent>
        <Box
          sx={{
            mb: 5,
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >         
        </Box>

        {graphData.map((item, index) => {
          return (
            <Box
              key={item.title}
              sx={{
                display: "flex",
                alignItems: "center",
                mb: index !== graphData.length - 1 ? 5.5 : undefined,
              }}
            >
              <Avatar
                variant="rounded"
                sx={{
                  mr: 3,
                  width: 50,
                  height: 42,
                  backgroundColor: "background.default",
                }}
              >
                <img
                  alt="avatar"
                  src={item.src}
                  width={item.imgWidth}
                  height={item.imgHeight}
                />
              </Avatar>
              <Box
                sx={{
                  width: "100%",
                  display: "flex",
                  flexWrap: "wrap",
                  alignItems: "center",
                  justifyContent: "space-between",
                }}
              >
                <Box
                  sx={{
                    mr: 2,
                    display: "flex",
                    mb: 0.4,
                    flexDirection: "column",
                  }}
                >
                  <Typography
                    variant="body2"
                    sx={{ mb: 0.5, fontWeight: 600, color: "text.primary" }}
                  >
                    {item.title}
                  </Typography>
                  <Typography variant="caption">{item.subtitle}</Typography>
                </Box>
                <CustomChip
                  skin="light"
                  size="small"
                  color="primary"
                  label={`${item.chipText} Tokens`}
                  sx={{ height: 20, fontSize: "0.75rem", fontWeight: 500 }}
                />
              </Box>
            </Box>
          );
        })}
      </CardContent>
    </Card>
  );
};

export default ProvidedByProcessorAgent;
