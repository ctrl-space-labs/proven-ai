// ** MUI Imports
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import Typography from "@mui/material/Typography";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import Grid from "@mui/material/Grid";
import Checkbox from "@mui/material/Checkbox";
import CustomChip from "src/views/custom-components/mui/chip";
import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useTheme } from "@mui/material/styles";
import { updateProvidedByProcessorAgents } from "src/store/userDataForAnalytics/userDataForAnalytics";
import Icon from "src/views/custom-components/mui/icon/icon";

const ProvidedByProcessorAgent = () => {
  const theme = useTheme();
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
        subheader={
          <span>
            Total{" "}
            <span
              style={{ fontWeight: "bold", color: theme.palette.primary.main }}
            >
              {totalTokensProvided}
            </span>{" "}
            Tokens Provided to other agents!
          </span>
        }
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
                  textAlign: "left",
                  opacity: item.active ? 1 : 0.5, // Change opacity for inactive agents
                  borderRadius: "5px", // Optional: add some border radius for better visual appearance
                  mb: index !== agents.length - 1 ? 6 : undefined,
                }}
              >
                <Checkbox
                  checked={item.active}
                  onChange={() => handleCheckboxChange(item.id)}
                />

                <Icon
                  icon="mdi:robot-happy-outline"
                  fontSize={40}
                  color={theme.palette.primary.main}
                />

                <Box
                  sx={{
                    width: "100%",
                    display: "flex",
                    flexWrap: "wrap",
                    alignItems: "center",
                    justifyContent: "space-between",
                    ml: 2,
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
                    variant='outlined'
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
