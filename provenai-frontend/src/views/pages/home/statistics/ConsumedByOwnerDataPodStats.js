import Card from "@mui/material/Card";
import { useTheme } from "@mui/material/styles";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import ReactApexcharts from "src/@core/components/react-apexcharts";
import { updateConsumedByOwnerDataPods } from "src/store/userDataForAnalytics/userDataForAnalytics";

import { hexToRGBA } from "src/@core/utils/hex-to-rgba";
import { useEffect } from "react";
import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";

const ConsumedByOwnerDataPodsStats = () => {
  // ** Hook
  const theme = useTheme();
  const dispatch = useDispatch();
  const [tokensPerOwnerDataPod, setTokensPerOwnerDataPod] = useState([]);
  const [dataPods, setDataPods] = useState([]);
  const [totalTokensConsumed, setTotalTokensConsumed] = useState(0);

  const consumedByOwnerDataPods = useSelector(
    (state) => state.userDataForAnalytics.analyticsData.consumedByOwnerDataPods
  );

  useEffect(() => {
    if (!consumedByOwnerDataPods) {
      return;
    }

    setDataPods(consumedByOwnerDataPods);

    const dataPodsStatsData = [
      {
        name: "Tokens",
        data: consumedByOwnerDataPods.map((dataPod) =>
          dataPod.active ? dataPod?.data[0] || 0 : 0
        ),
      },
    ];

    const tokensData = consumedByOwnerDataPods
      .filter((dataPod) => dataPod.active)
      .reduce((acc, dataPod) => acc + (dataPod.data[0] || 0), 0);

    setTokensPerOwnerDataPod(dataPodsStatsData);
    setTotalTokensConsumed(tokensData);
  }, [consumedByOwnerDataPods]);

  const handleLegendClick = (chartContext, seriesIndex, config) => {
    const updatedDataPods = dataPods.map((dataPod, index) => {
      if (index === seriesIndex) {
        return { ...dataPod, active: !dataPod.active };
      }
      return dataPod;
    });

    dispatch(updateConsumedByOwnerDataPods(updatedDataPods));
  };

  const lightPalette = [
    theme.palette.warning.light,
    theme.palette.info.light,
    theme.palette.error.light,
    theme.palette.primary.light,
    theme.palette.success.light,
  ];

  const darkPalette = [
    theme.palette.warning.dark,
    theme.palette.info.dark,
    theme.palette.error.dark,
    theme.palette.primary.dark,
    theme.palette.success.dark,
  ];

  const colorPalette = dataPods.map((dataPod, index) => {
    const colors = theme.palette.mode === 'light' ? lightPalette : darkPalette;
    if (dataPod.active) {
      return hexToRGBA(colors[index % colors.length], 1);
    } else {
      return hexToRGBA(theme.palette.grey[400], 1);
    }
  });

  const options = {
    chart: {
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
            filename: "consumed-stats",
            columnDelimiter: ",",
            headerCategory: "category",
            headerValue: "value",
            dateFormatter(timestamp) {
              return new Date(timestamp).toDateString();
            },
          },
          svg: {
            filename: "consumed-stats",
          },
          png: {
            filename: "consumed-stats",
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

    dataLabels: {
      enabled: true,
      offsetY: 8,
      offsetX: -20,
      textAnchor: "start",
      style: {
        fontWeight: 500,
        fontSize: "0.875rem",
        colors: [function(opts) {
          return theme.palette.mode === 'light' ? theme.palette.primary.dark : theme.palette.common.white;
        }],
      },
      formatter: function (val, opt) {
        return (
          opt.w.globals.labels[opt.dataPointIndex] + ":  " + val + " Tokens"
        );
      },
    },

    legend: {
      show: true,
      position: "top",
      horizontalAlign: "center",

      itemMargin: {
        vertical: 5,
        horizontal: 15,
      },
      labels: {
        colors: theme.palette.mode === 'light' ? theme.palette.text.primary : theme.palette.grey[400],
      },
      markers: {
        offsetX: -5,
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
        top: 0,
        left: 20,
        right: 30,
        bottom: 0,
      },
    },
    colors: colorPalette,

    xaxis: {
      axisTicks: { show: false },
      axisBorder: { show: false },
      categories: dataPods.map((dataPod) => dataPod.name),

      labels: {
        formatter: (val) => `${Number(val) / 1000}K`,
        style: {
          fontSize: "0.875rem",
          colors: theme.palette.text.disabled,
        },
      },
    },
    yaxis: {
      labels: {
        show: false,
      },
    },
  };






  return (
    <Card sx={{ backgroundColor: "transparent" }}>
      <CardHeader
        title="Data Pods consumed by you!"
        subheader={
          <span>
            Total{" "}
            <span
              style={{ fontWeight: "bold", color: theme.palette.primary.main }}
            >
              {totalTokensConsumed}
            </span>{" "}
            Tokens Consumed from others!
          </span>
        }
        subheaderTypographyProps={{ sx: { lineHeight: 1.5 } }}
        titleTypographyProps={{ sx: { letterSpacing: "0.15px" } }}
        sx={{ textAlign: "left", p: 3 }}
      />
      <CardContent sx={{ p: "0 !important" }}>
        <ReactApexcharts
          series={tokensPerOwnerDataPod}
          options={options}
          height={300}
          type="bar"
        />
      </CardContent>
    </Card>
  );
};

export default ConsumedByOwnerDataPodsStats;
