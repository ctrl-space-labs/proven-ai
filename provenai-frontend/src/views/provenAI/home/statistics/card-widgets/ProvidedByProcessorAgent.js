// ** MUI Imports
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import Grid from "@mui/material/Grid";
import Checkbox from "@mui/material/Checkbox";
import CustomChip from "src/@core/components/mui/chip";
import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { updateProvidedByProcessorAgents } from "src/store/apps/userDataForAnalytics/userDataForAnalytics";

const ProvidedByProcessorAgent = () => {
  const dispatch = useDispatch();

  const [agents, setAgents] = useState([]);
  const [totalTokensProvided, setTotalTokensProvided] = useState(0);

  const providedByProcessorAgents = useSelector(
    (state) =>
      state.userDataForAnalytics.analyticsData.providedByProcessorAgents
  );

  useEffect(() => {
    if (!providedByProcessorAgents) {
      return;
    }

    setAgents(providedByProcessorAgents);

    const totalTokens = providedByProcessorAgents
      .filter((agent) => agent.active)
      .reduce((acc, agent) => acc + (agent.data[0] || 0), 0);

    setTotalTokensProvided(totalTokens);
  }, [providedByProcessorAgents]);

  const handleCheckboxChange = (id) => {
    const updatedAgents = agents.map((agent) =>
      agent.id === id ? { ...agent, active: !agent.active } : agent
    );

    dispatch(updateProvidedByProcessorAgents(updatedAgents));
  };

  return (
    <Card sx={{ backgroundColor: "transparent" }}>
      <CardHeader
        title="Agents Provided Data To my Data pods "
        subheader={`Total ${totalTokensProvided} Tokens Provided to other agents!`}
        sx={{ textAlign: "left", p: 3 }}
      />
      <CardContent>
        <Box
          sx={{
            mb: 2,
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        ></Box>

        <Grid container spacing={2}>
          {agents?.map((item, index) => {
            return (
              <Box
                key={item.name}
                sx={{
                  display: "flex",
                  alignItems: "center",
                  mb: index !== agents.length - 1 ? 5.5 : undefined,
                  opacity: item.active ? 1 : 0.5, // Change opacity for inactive agents
                  borderRadius: "4px", // Optional: add some border radius for better visual appearance
                  textAlign: "left",
                }}
              >
                <Checkbox
                  checked={item.active}
                  onChange={() => handleCheckboxChange(item.id)}
                />

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
                    src={"/images/avatars/3.png"}
                    width={44}
                    height={44}
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
                      {item.name}
                    </Typography>
                  </Box>
                  <CustomChip
                    skin="light"
                    size="small"
                    color="primary"
                    label={`${item.data[0] || 0} Tokens`}
                    sx={{ height: 20, fontSize: "0.75rem", fontWeight: 500 }}
                  />
                </Box>
              </Box>
            );
          })}
        </Grid>
      </CardContent>
    </Card>
  );
};

export default ProvidedByProcessorAgent;
