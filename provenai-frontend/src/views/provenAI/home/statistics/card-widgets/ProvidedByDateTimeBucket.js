import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import Button from "@mui/material/Button";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import { useTheme } from "@mui/material/styles";
import CardHeader from "@mui/material/CardHeader";
import Typography from "@mui/material/Typography";
import CardContent from "@mui/material/CardContent";
import OptionsMenu from "src/@core/components/option-menu";
import ReactApexcharts from "src/@core/components/react-apexcharts";
import { hexToRGBA } from "src/@core/utils/hex-to-rgba";
import { set } from "nprogress";

const ProvidedByDateTimeBucket = () => {
  const theme = useTheme();

  const [totalTokensProvided, setTotalTokensProvided] = useState(0);
  const [totalTokensUpdated, setTotalTokensUpdated] = useState(0);

  const providedByDataTimeBucket = useSelector(
    (state) =>
      state.userDataForAnalytics.analyticsData.providedByDateTimeBuckets
  );

  useEffect(() => {
    if (!providedByDataTimeBucket) {
      return;
    }

    const totalTokens = providedByDataTimeBucket.reduce(
      (acc, item) => acc + (item.totalSumTokens || 0),
      0
    );

    const totalTokensUpdated = providedByDataTimeBucket.reduce(
      (acc, item) => acc + (item.updatedTotalTokens || 0),
      0
    );

    setTotalTokensProvided(totalTokens);
    setTotalTokensUpdated(totalTokensUpdated);
  }, [providedByDataTimeBucket]);

  const filterDataByTimeRange = () => {
    const filteredData = [];
    const filteredUpdatedData = [];
    const categories = [];
    const currentDate = new Date();
    const oneDay = 24 * 60 * 60 * 1000;

    for (let i = 0; i < 30; i++) {
      const date = new Date(currentDate - i * oneDay);
      const dateString = date.toISOString().split("T")[0] + "T00:00:00Z";
      const formattedDate = `${date.getDate()}/${date.getMonth() + 1}`;
      categories.unshift(formattedDate);
      const matchingBucket = providedByDataTimeBucket.find(
        (bucket) => bucket.date === dateString
      );
      filteredData.unshift(matchingBucket ? matchingBucket.totalSumTokens : 0);
      filteredUpdatedData.unshift(
        matchingBucket ? matchingBucket.updatedTotalTokens : 0
      );
    }

    return { filteredData, filteredUpdatedData, categories };
  };

  const { filteredData, filteredUpdatedData, categories } =
    filterDataByTimeRange();

  const series = [
    {
      name: "Total Tokens",
      type: "column",
      data: filteredData,
    },
    {
      name: "Selected DataPods and Agents Tokens",
      type: "line",
      data: filteredUpdatedData,
    },
  ];

  const options = {
    chart: {
      offsetY: -9,
      offsetX: -16,
      parentHeightOffset: 0,
      toolbar: { show: false },
    },
    plotOptions: {
      bar: {
        borderRadius: 9,
        columnWidth: "50%",
        endingShape: "rounded",
        startingShape: "rounded",
        colors: {
          ranges: [
            {
              to: 50,
              from: 40,
              color: hexToRGBA(theme.palette.primary.main, 1),
            },
          ],
        },
      },
    },
    markers: {
      size: 3.5,
      strokeWidth: 2,
      fillOpacity: 1,
      strokeOpacity: 1,
      colors: [theme.palette.background.paper],
      strokeColors: hexToRGBA(theme.palette.primary.main, 1),
    },
    stroke: {
      width: [0, 2],
      colors: [theme.palette.customColors.trackBg, theme.palette.primary.main],
    },
    legend: { show: false },
    dataLabels: { enabled: false },
    colors: [hexToRGBA(theme.palette.customColors.trackBg, 1)],
    grid: {
      strokeDashArray: 7,
      borderColor: theme.palette.divider,
    },
    states: {
      hover: {
        filter: { type: "none" },
      },
      active: {
        filter: { type: "none" },
      },
    },
    xaxis: {
      categories: categories,
      tickPlacement: "on",
      labels: { show: true },
      axisTicks: { show: false },
      axisBorder: { show: false },
    },
    yaxis: {
      min: 0,
      max: Math.max(...filteredData) + 10,
      show: true,
      tickAmount: 3,
      labels: {
        formatter: (value) =>
          `${value > 999 ? `${(value / 1000).toFixed(0)}` : value}k`,
        style: {
          fontSize: "0.75rem",
          colors: theme.palette.text.disabled,
        },
      },
    },
  };

  return (
    <Card sx={{ backgroundColor: "transparent" }}>
      <CardHeader title="Overview" sx={{ textAlign: "left"}} />
      <CardContent
        sx={{ "& .apexcharts-xcrosshairs.apexcharts-active": { opacity: 0 } }}
      >
        <ReactApexcharts
          type="line"
          height={208}
          series={series}
          options={options}
        />
        <Box sx={{ mb: 4, display: "flex", alignItems: "center" }}>
          <Typography sx={{ mr: 4 }} variant="h5">
            {totalTokensProvided} Tokens
          </Typography>
          <Typography variant="body2">
            Your performance over the selected period.
          </Typography>
        </Box>
        <Box sx={{ mb: 4, display: "flex", alignItems: "center" }}>
          <Typography sx={{ mr: 4 }} variant="h5">
            {totalTokensUpdated} Tokens
          </Typography>
          <Typography variant="body2">
            Your performance over the selected items.
          </Typography>
        </Box>
      </CardContent>
    </Card>
  );
};

export default ProvidedByDateTimeBucket;
