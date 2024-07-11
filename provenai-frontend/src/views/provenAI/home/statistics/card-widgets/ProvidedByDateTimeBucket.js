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

  const filteredDates = useSelector(
    (state) => state.permissionOfUseAnalytics.filters.apiFilters
  );
  
  console.log("------>",filteredDates);

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
    // if (!filteredDates || !providedByDataTimeBucket) {
    //   return { filteredData: [], filteredUpdatedData: [], categories: [] };
    // }

    const { from, to, timeIntervalInSeconds } = filteredDates;
  const startDate = new Date(from);
  const endDate = new Date(to);
  const timeDiff = endDate - startDate;
  const oneDay = 24 * 60 * 60 * 1000;

  let interval, formatCategory;

  if (timeDiff <= oneDay) {
    // Interval is less than or equal to one day
    interval = 60 * 60 * 1000; // 1 hour in milliseconds
    formatCategory = (date) => `${date.getHours()}:00`;
  } else if (timeDiff <= 30 * oneDay) {
    // Interval is less than or equal to 30 days
    interval = oneDay;
    formatCategory = (date) => `${date.getDate()}/${date.getMonth() + 1}`;
  } else if (timeDiff <= 365 * oneDay) {
    // Interval is less than or equal to 1 year
    interval = 30 * oneDay; // 1 month approximation
    formatCategory = (date) => `${date.getMonth() + 1}/${date.getFullYear()}`;
  } else {
    // Interval is more than 1 year
    interval = 365 * oneDay; // 1 year approximation
    formatCategory = (date) => `${date.getFullYear()}`;
  }

  const filteredData = [];
  const filteredUpdatedData = [];
  const categories = [];

  for (let date = startDate; date <= endDate; date = new Date(date.getTime() + interval)) {
    const dateString = date.toISOString().split("T")[0] + "T00:00:00Z";
    const formattedDate = formatCategory(date);

    categories.push(formattedDate);

    const matchingBucket = providedByDataTimeBucket.find(
      (bucket) => bucket.date === dateString
    );

    filteredData.push(matchingBucket ? matchingBucket.totalSumTokens : 0);
    filteredUpdatedData.push(
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
      name: "Selected Tokens",
      type: "line",
      data: filteredUpdatedData,
    },
  ];

  const options = {
    chart: {
      offsetY: -9,
      offsetX: -16,
      parentHeightOffset: 0,

      toolbar: {
        show: true,
        offsetX: 0,
        offsetY: -40,
        tools: {
          download: true,
          selection: false,
          zoom: false,
          zoomin: true,
          zoomout: true,
          pan: false,
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
          `${value > 999 ? `${(value / 1000).toFixed(0)}k` : value}`,
        style: {
          fontSize: "0.75rem",
          colors: theme.palette.text.disabled,
        },
      },
    },
  };

  return (
    <Card sx={{ backgroundColor: "transparent" }}>
      <CardHeader
        title="Overview"
        subheader={
          <span>
            Total{" "}
            <span
              style={{ fontWeight: "bold", color: theme.palette.primary.main }}
            >
              {totalTokensProvided}
            </span> 
            {" "} Tokens From all DataPods and Agents in the selected period.
            <br/> Total {" "}
            <span
              style={{ fontWeight: "bold", color: theme.palette.primary.main }}
            >
              {totalTokensUpdated}
            </span>{" "}
            Tokens From selected DataPods and Agents
          </span>
        }
        sx={{ textAlign: "left" }}
      />
      <CardContent
        sx={{ "& .apexcharts-xcrosshairs.apexcharts-active": { opacity: 0 } }}
      >
        <ReactApexcharts
          type="line"
          height={208}
          series={series}
          options={options}
        />
        
      </CardContent>
    </Card>
  );
};

export default ProvidedByDateTimeBucket;
