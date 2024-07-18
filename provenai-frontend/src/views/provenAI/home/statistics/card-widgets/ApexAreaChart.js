import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";

import Card from "@mui/material/Card";
import { useTheme } from "@mui/material/styles";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";

// ** Component Import
import ReactApexcharts from "src/@core/components/react-apexcharts";

// ** Util Import
import { hexToRGBA } from "src/@core/utils/hex-to-rgba";

const radialBarColors = {
  series1: "#fdd835",
  series2: "#40CDFA",
  series3: "#00d4bd",
  series4: "#7367f0",
  series5: "#FFA1A1",
  series6: "#FFDB9B",
  series7: "#b8c2cc",
  series8: "#f77e53",
};

const ApexRadialBarChart = () => {
  // ** Hook
  const theme = useTheme();
  const dispatch = useDispatch();
  const [dataPods, setDataPods] = useState([]);
  const [totalTokensProvided, setTotalTokensProvided] = useState(0);
  const [tokensPerOwnerDataPod, setTokensPerOwnerDataPod] = useState([]);

  

  const providedByOwnerDataPods = useSelector(
    (state) => state.userDataForAnalytics.analyticsData.providedByOwnerDataPods
  );

  useEffect(() => {
    if (!providedByOwnerDataPods) {
      return;
    }

    setDataPods(providedByOwnerDataPods);

    const dataPodsStatsData = providedByOwnerDataPods
      .filter((dataPod) => dataPod.active)
      .map((dataPod) => dataPod?.data[0] || 0);

    const tokensData = providedByOwnerDataPods
      .filter((dataPod) => dataPod.active)
      .reduce((acc, dataPod) => acc + (dataPod.data[0] || 0), 0);

    setTokensPerOwnerDataPod(dataPodsStatsData);
    setTotalTokensProvided(tokensData);
  }, [providedByOwnerDataPods]);

  const options = {
    stroke: { lineCap: "round" },
    labels: dataPods.map((dataPod) => dataPod.name),
    legend: {
      show: true,
      position: "bottom",
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
    // colors: [radialBarColors.series1, radialBarColors.series2, radialBarColors.series4],
    colors: dataPods.map((dataPod, index) => {
      return dataPod.active
        ? radialBarColors[`series${index + 1}`]
        : theme.palette.divider;
    }),
    

    plotOptions: {
      radialBar: {
        offsetY: 0,
        startAngle: 0,
        endAngle: 270,
        hollow: {
          margin: 5,
          size: "30%",
          background: "transparent",
          image: undefined,
        },
        track: {
          margin: 15,
          background: hexToRGBA(theme.palette.customColors.trackBg, 1),
        },
        dataLabels: {
          show: true,
          name: {
            show: true,
            fontSize: '16px',
            color: theme.palette.text.primary,
          },
          value: {
            show: true, // Added this line to ensure value labels are shown
            fontSize: '14px',
            color: theme.palette.text.secondary,
            // formatter: function (val, opts) { // Added formatter function for value
            //   return opts.w.globals.series[opts.seriesIndex];
            // }
          },
        },
        barLabels: {
          enabled: true,
          useSeriesColors: true,
          margin: 8,
          fontSize: "16px",
          formatter: function (seriesName, opts) {
            
            const dataPodName = dataPods[opts.seriesIndex].name;
            return dataPodName + ": " + opts.w.globals.series[opts.seriesIndex];
          },
        },
          total: {
            show: true,
            fontWeight: 400,
            label: "Tokens",
            fontSize: "1.125rem",
            color: theme.palette.text.primary,
            formatter: function (w) { // Added formatter function for total
              const totalValue = w.globals.seriesTotals.reduce((a, b) => a + b, 0);
              return totalValue;
            },
          
        },
      },
    },
    responsive: [
      {
        breakpoint: 480,
      },
    ],
    grid: {
      padding: {
        top: -35,
        bottom: -30,
      },
    },
  };

  return (
    <Card>
      <CardHeader title="Statistics" />
      <CardContent>
        <ReactApexcharts
          type="radialBar"
          height={400}
          options={options}
          series={tokensPerOwnerDataPod}
        />
      </CardContent>
    </Card>
  );
};

export default ApexRadialBarChart;
