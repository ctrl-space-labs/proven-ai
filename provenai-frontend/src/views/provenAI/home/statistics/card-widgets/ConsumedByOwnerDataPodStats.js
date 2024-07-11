import Card from "@mui/material/Card";
import { useTheme } from "@mui/material/styles";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import OptionsMenu from "src/@core/components/option-menu";
import ReactApexcharts from "src/@core/components/react-apexcharts";
import { updateConsumedByOwnerDataPods } from "src/store/apps/userDataForAnalytics/userDataForAnalytics";

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

  const colorPalette = dataPods.map((dataPod, index) => {
    if (dataPod.active) {
      const colors = [
        hexToRGBA(theme.palette.primary.light, 1),
        hexToRGBA(theme.palette.success.light, 1),
        hexToRGBA(theme.palette.warning.light, 1),
        hexToRGBA(theme.palette.info.light, 1),
        hexToRGBA(theme.palette.error.light, 1),
      ];
      return colors[index % colors.length];
    } else {
      return hexToRGBA(theme.palette.grey[400], 1);
    }
  });

  const options = {
    chart: {
      parentHeightOffset: 0,
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
    },

    dataLabels: {
      offsetY: 8,
      offsetX: -20,
      textAnchor: "start",
      style: {
        fontWeight: 500,
        fontSize: "0.875rem",
      },
      formatter: function (val, opt) {
        return (
          opt.w.globals.labels[opt.dataPointIndex] + ":  " + val + " Tokens"
        );
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
      categories: dataPods.map((dataPod) => dataPod.name),

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
        show: false,
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
        subheaderTypographyProps={{ sx: { lineHeight: 1.429 } }}
        titleTypographyProps={{ sx: { letterSpacing: "0.15px" } }}
        sx={{ textAlign: "left", p: 3 }}
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
