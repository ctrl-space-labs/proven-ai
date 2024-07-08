// ** MUI Imports
import Card from "@mui/material/Card";
import { useTheme } from "@mui/material/styles";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";

// ** Custom Components Imports
import OptionsMenu from "src/@core/components/option-menu";
import ReactApexcharts from "src/@core/components/react-apexcharts";

// ** Util Import
import { hexToRGBA } from "src/@core/utils/hex-to-rgba";
import { useEffect } from "react";
import { useState } from "react";
import { useSelector } from "react-redux";
import dataPodsService from "src/provenAI-sdk/dataPodsService";
import authConfig from "src/configs/auth";

const ConsumedByOwnerDataPodsStats = () => {
  // ** Hook
  const theme = useTheme();

  const [tokensPerOwnerDataPod, setTokensPerOwnerDataPod] = useState([]);
  const [dataPods, setDataPods] = useState([]);
  const [totalTokensConsumed, setTotalTokensConsumed] = useState(0);
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );
  const permissionOfUseAnalytics = useSelector(
    (state) => state.permissionOfUseAnalytics
  );

  useEffect(() => {
    console.log("graph data updated! ", permissionOfUseAnalytics.graphData);
    if (!permissionOfUseAnalytics.graphData) {
      return;
    }

    const ids = Object.keys(
      permissionOfUseAnalytics.graphData.consumedDataTokensByOwnerDataPod
    );

    const fetchUserDataPods = async () => {
      try {
        const idsParam = ids.join(",");
        const userDataPodsResponse = await dataPodsService.getDataPodsByIdIn(
          idsParam,
          storedToken
        );
        setDataPods(userDataPodsResponse.data.content);
      } catch (error) {
        console.error("Error fetching user data pods: ", error);
      }
    };

    fetchUserDataPods();
  }, [permissionOfUseAnalytics.graphData, storedToken]);

  useEffect(() => {
    if (!permissionOfUseAnalytics.graphData || dataPods.length === 0) {
      return;
    }

    const dataPodsStatsData = [
      {
        name: "Tokens",
        data: dataPods.map(
          (dataPod) =>
            permissionOfUseAnalytics.graphData.consumedDataTokensByOwnerDataPod[
              dataPod.id
            ]?.totalSumTokens || 0
        ),
      },
    ];

    const totalTokens = dataPodsStatsData[0].data.reduce(
      (acc, item) => acc + item,
      0
    );

    setTotalTokensConsumed(totalTokens);
    setTokensPerOwnerDataPod(dataPodsStatsData);
  }, [dataPods, permissionOfUseAnalytics.graphData]);

  const options = {
    chart: {
      parentHeightOffset: 0,
      toolbar: { show: false },
    },
    plotOptions: {
      bar: {
        borderRadius: 8,
        barHeight: "60%",
        horizontal: true,
        distributed: true,
        startingShape: "rounded",
      },
    },
    dataLabels: {
      offsetY: 8,
      style: {
        fontWeight: 500,
        fontSize: "0.875rem",
      },
    },
    grid: {
      strokeDashArray: 8,
      borderColor: theme.palette.divider,
      xaxis: {
        lines: { show: true },
      },
      yaxis: {
        lines: { show: false },
      },
      padding: {
        top: -18,
        left: 21,
        right: 33,
        bottom: 10,
      },
    },
    colors: [
      hexToRGBA(theme.palette.primary.light, 1),
      hexToRGBA(theme.palette.success.light, 1),
      hexToRGBA(theme.palette.warning.light, 1),
      hexToRGBA(theme.palette.info.light, 1),
      hexToRGBA(theme.palette.error.light, 1),
    ],
    legend: { show: false },
    states: {
      hover: {
        filter: { type: "none" },
      },
      active: {
        filter: { type: "none" },
      },
    },
    xaxis: {
      axisTicks: { show: false },
      axisBorder: { show: false },
      categories: dataPods.map((dataPod) => dataPod.podUniqueName),

      labels: {
        formatter: (val) => `${Number(val) / 1000}k`,
        style: {
          fontSize: "0.875rem",
          colors: theme.palette.text.disabled,
        },
      },
    },
    yaxis: {
      labels: {
        align: theme.direction === "rtl" ? "right" : "left",
        style: {
          fontWeight: 600,
          fontSize: "0.875rem",
          colors: theme.palette.text.primary,
        },
      },
    },
  };

  return (
    <Card>
      <CardHeader
        title="Data Pods consumed by you!"
        subheader={`Total ${totalTokensConsumed} Tokens Consumed from others!`}
        subheaderTypographyProps={{ sx: { lineHeight: 1.429 } }}
        titleTypographyProps={{ sx: { letterSpacing: "0.15px" } }}
        action={
          <OptionsMenu
            options={["Last 28 Days", "Last Month", "Last Year"]}
            iconButtonProps={{ size: "small", className: "card-more-options" }}
          />
        }
      />
      <CardContent sx={{ p: "0 !important" }}>
        <ReactApexcharts
          type="bar"
          height={294}
          series={tokensPerOwnerDataPod}
          options={options}
        />
      </CardContent>
    </Card>
  );
};

export default ConsumedByOwnerDataPodsStats;
