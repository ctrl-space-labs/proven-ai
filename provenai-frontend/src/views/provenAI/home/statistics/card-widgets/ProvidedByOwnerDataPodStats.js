import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useTheme } from "@mui/material/styles";
import Card from "@mui/material/Card";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import Box from "@mui/material/Box";
import Chip from "@mui/material/Chip";

import OptionsMenu from "src/@core/components/option-menu";
import ReactApexcharts from "src/@core/components/react-apexcharts";

import { hexToRGBA } from "src/@core/utils/hex-to-rgba";

const ProvidedByOwnerDataPodsStats = () => {
  const theme = useTheme();
  const [tokensPerOwnerDataPod, setTokensPerOwnerDataPod] = useState([]);
  const [dataPods, setDataPods] = useState([]);
  const [totalTokensProvided, setTotalTokensProvided] = useState(0);

  const providedByOwnerDataPods = useSelector(
    (state) => state.userDataForAnalytics.analyticsData.providedByOwnerDataPods
  );

  console.log("providedByOwnerDataPods: ", providedByOwnerDataPods);

  useEffect(() => {
    if (!providedByOwnerDataPods) {
      return;
    }

    setDataPods(providedByOwnerDataPods);    

    const tokensData = providedByOwnerDataPods
      .filter((dataPod) => dataPod.active)
      .map((dataPod) => dataPod.data || 0);      

    setTokensPerOwnerDataPod([{ name: "Tokens", data: tokensData }]);
    setTotalTokensProvided(tokensData.reduce((acc, tokens) => acc + tokens, 0));
  }, [providedByOwnerDataPods]);

  const handleLegendClick = ( chartContext, seriesIndex, config) => {    
    console.log("seriesIndex: ", seriesIndex);      
    const updatedDataPods = dataPods.map((dataPod, index) => {
      if (index === seriesIndex) {
        return { ...dataPod, active: !dataPod.active };
      }
      return dataPod;
    });
    
    console.log("updatedDataPods: ", updatedDataPods);    
    setDataPods(updatedDataPods);

    console.log("updatedDataPods: ", updatedDataPods);
    const activeTokensData = updatedDataPods
      .filter((dataPod) => dataPod.active)
      .map((dataPod) => dataPod.totalSumTokens || 0);
    setTokensPerOwnerDataPod([{ name: "Tokens", data: activeTokensData }]);
    setTotalTokensProvided(activeTokensData.reduce((acc, tokens) => acc + tokens, 0));
  };

  const colorPalette = dataPods.map((dataPod) =>
    dataPod.active
      ? hexToRGBA(theme.palette.primary.light, 1)
      : hexToRGBA(theme.palette.grey[400], 1)
  );

  const options = {
    chart: {
      parentHeightOffset: 0,
      // events: {
      //   legendClick: function (chartContext, seriesIndex, config) {
      //     console.log("legendClick: ", chartContext, seriesIndex, config);
      //   },
      // },

      events: {
        legendClick: handleLegendClick,
      },

      toolbar: {
        show: true,
        offsetX: 0,
        offsetY: -70,
        tools: {
          download: true,
          selection: true,          
        },
        export: {
          csv: {
            filename: undefined,
            columnDelimiter: ",",
            headerCategory: "category",
            headerValue: "value",
            dateFormatter(timestamp) {
              return new Date(timestamp).toDateString();
            },
          },
          svg: {
            filename: undefined,
          },
          png: {
            filename: undefined,
          },
        },
        autoSelected: "zoom",
      },

      
    },
    plotOptions: {
      bar: {
        borderRadius: 8,
        barHeight: "40%",
        horizontal: true,
        distributed: true,
        startingShape: "rounded",
      },
    },

    legend: {
      show: true,
      position: "top",
      horizontalAlign: "center",
      labels: {
        colors: theme.palette.text.secondary,
      },
      markers: {
        offsetX: -3,
      },
      itemMargin: {
        vertical: 3,
        horizontal: 10,
      },

      // events: {
      //   click: function (event, chartContext, config) {
      //     const podIndex = config.dataPointIndex;
      //     handlePodClick(podIndex);
      //     console.log("podIndex: &&&&&&&&&&&&&&&&&&", podIndex);
      //   },
      // },

      onItemClick: {
        toggleDataSeries: true,
      },
      onItemHover: {
        highlightDataSeries: true,
      },
    },

    dataLabels: {
      offsetY: 8,
      style: {
        fontWeight: 500,
        fontSize: "0.875rem",
      },
    },

    states: {
      normal: {
        filter: {
          type: "none",
          value: 0,
        },
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
    colors: colorPalette,
    
    xaxis: {
      axisTicks: { show: false },
      axisBorder: { show: false },
      categories: dataPods.filter((dataPod) => dataPod.active).map((dataPod) => dataPod.name),
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
        title="Your Data Pods Statistics"
        subheader={`Total ${totalTokensProvided} Tokens Provided to others!`}
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
          series={dataPods}
          options={options}
        />
      </CardContent>
    </Card>
  );
};

export default ProvidedByOwnerDataPodsStats;
