import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import Card from "@mui/material/Card";
import { useTheme } from "@mui/material/styles";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import ReactApexcharts from "src/@core/components/react-apexcharts";
import { hexToRGBA } from "src/@core/utils/hex-to-rgba";

const ConsumedByDateTimeBucket = () => {
  const theme = useTheme();
  const [totalTokensProvided, setTotalTokensProvided] = useState(0);
  const [totalTokensUpdated, setTotalTokensUpdated] = useState(0);

  const consumedByDataTimeBucket = useSelector(
    (state) =>
      state.userDataForAnalytics.analyticsData.consumedByDateTimeBuckets
  );
  const filteredDates = useSelector(
    (state) => state.permissionOfUseAnalytics.filters.apiFilters
  );

  useEffect(() => {
    if (!consumedByDataTimeBucket) {
      return;
    }

    const totalTokens = consumedByDataTimeBucket.reduce(
      (acc, item) => acc + (item.totalSumTokens || 0),
      0
    );

    const totalTokensUpdated = consumedByDataTimeBucket.reduce(
      (acc, item) => acc + (item.updatedTotalTokens || 0),
      0
    );

    setTotalTokensProvided(totalTokens);
    setTotalTokensUpdated(totalTokensUpdated);
  }, [consumedByDataTimeBucket]);

  const aggregateDataByInterval = (startDate, endDate, interval, formatCategory) => {
    const aggregatedData = {};
    const categories = [];
  
    // Initialize the dates and loop through each interval
    for (let date = new Date(startDate); date <= endDate; date = new Date(date.getTime() + interval)) {
      const category = formatCategory(date);
      categories.push(category);
  
      aggregatedData[category] = {
        totalSumTokens: 0,
        updatedTotalTokens: 0,
      };
    }
  
    // Aggregate the data based on the provided buckets
    consumedByDataTimeBucket.forEach((bucket) => {
      const date = new Date(bucket.date);
      const category = formatCategory(date);
  
      if (aggregatedData[category]) {
        aggregatedData[category].totalSumTokens += bucket.totalSumTokens;
        aggregatedData[category].updatedTotalTokens += bucket.updatedTotalTokens;
      }
    });
  
    return {
      filteredData: categories.map((category) => aggregatedData[category].totalSumTokens),
      filteredUpdatedData: categories.map((category) => aggregatedData[category].updatedTotalTokens),
      categories,
    };
  };


  const filterDataByTimeRange = () => {
    if (!filteredDates || !consumedByDataTimeBucket) {
      return { filteredData: [], filteredUpdatedData: [], categories: [] };
    }

    const { from, to } = filteredDates;
    const startDate = new Date(from);
    const endDate = new Date(to);
    const timeDiff = endDate - startDate;
    const oneDay = 24 * 60 * 60 * 1000;

    if (timeDiff <= oneDay) {
      return aggregateDataByInterval(
        startDate, endDate, 
        60 * 60 * 1000, // 1 hour in milliseconds
        (date) => `${date.getHours()}:00`
      );
    } else if (timeDiff <= 30 * oneDay) {
      return aggregateDataByInterval(
        startDate, endDate, 
        oneDay,
        (date) => `${date.getDate()}/${date.getMonth() + 1}`
      );
    } else if (timeDiff <= 90 * oneDay) {
      return aggregateDataByInterval(
        startDate, endDate, 
         oneDay,
        (date) => `${date.getDate()}/${date.getMonth() + 1}`
      );
    } else if (timeDiff <= 180 * oneDay) {
      return aggregateDataByInterval(
        startDate, endDate, 
         oneDay,
        (date) => `${date.getDate()}/${date.getMonth() + 1}`
      );
    } else if (timeDiff <= 365 * oneDay) {
      return aggregateDataByInterval(
        startDate, endDate, 
        30 * oneDay,
        (date) => `${date.getMonth() + 1}/${date.getFullYear()}`
      );
    } else if (timeDiff <= 2 * 365 * oneDay) {
      return aggregateDataByInterval(
        startDate, endDate, 
        30 * oneDay,
        (date) => `${date.getMonth() + 1}/${date.getFullYear()}`
      );
    } else {
      return aggregateDataByInterval(
        startDate, endDate, 
        30 * oneDay,
        (date) => `${date.getMonth() + 1}/${date.getFullYear()}`
      );
    }
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
      toolbar: {
        show: true,
        offsetX: 0,
        offsetY: -40,
        tools: {
          download: true,
          selection: true,
          zoom: true,
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
      <CardHeader
        title="Overview"
        subheader={
          <span>
            Total{" "}
            <span
              style={{ fontWeight: "bold", color: theme.palette.primary.main }}
            >
              {totalTokensProvided}
            </span>{" "}
            Tokens From all DataPods and Agents in the selected period.
            <br /> Total{" "}
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

export default ConsumedByDateTimeBucket;
