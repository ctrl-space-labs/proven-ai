import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useTheme } from "@mui/material/styles";
import Card from "@mui/material/Card";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";
import ReactApexcharts from "src/@core/components/react-apexcharts";
import { hexToRGBA } from "src/@core/utils/hex-to-rgba";
import { updateProvidedByOwnerDataPods } from "src/store/apps/userDataForAnalytics/userDataForAnalytics";



const ProvidedByOwnerDataPodsStats = () => {
  const theme = useTheme();
  const dispatch = useDispatch();
  const [tokensPerOwnerDataPod, setTokensPerOwnerDataPod] = useState([]);
  const [dataPods, setDataPods] = useState([]);
  const [totalTokensProvided, setTotalTokensProvided] = useState(0);

  const providedByOwnerDataPods = useSelector(
    (state) => state.userDataForAnalytics.analyticsData.providedByOwnerDataPods
  );

   // Inject custom CSS for ApexCharts toolbar this works for all charts
 const menuColors =() =>{
  const style = document.createElement('style');
  style.innerHTML = `
    .apexcharts-menu {
      background-color: ${theme.palette.mode === 'light' ? '#fff' : '#333'};
      color: ${theme.palette.mode === 'light' ? '#000' : '#fff'};
    }
    .apexcharts-menu-item {
      color: ${theme.palette.mode === 'light' ? '#000' : '#fff'};
    }
    .apexcharts-menu-item:hover {
      background-color: ${theme.palette.action.hover};
    }
  `;
  document.head.appendChild(style);
  return () => {
    document.head.removeChild(style);
  };
}

  menuColors();
  
  useEffect(() => {
    menuColors();
  }, [theme.palette.mode]);


  useEffect(() => {
    if (!providedByOwnerDataPods) {
      return;
    }

    setDataPods(providedByOwnerDataPods);

    const dataPodsStatsData = [
      {
        name: "Tokens",
        data: providedByOwnerDataPods.map((dataPod) =>
          dataPod.active ? dataPod?.data[0] || 0 : 0
        ),
      },
    ];

    const tokensData = providedByOwnerDataPods
      .filter((dataPod) => dataPod.active)
      .reduce((acc, dataPod) => acc + (dataPod.data[0] || 0), 0);

    setTokensPerOwnerDataPod(dataPodsStatsData);
    setTotalTokensProvided(tokensData);
  }, [providedByOwnerDataPods]);

  const handleLegendClick = (chartContext, seriesIndex, config) => {
    const updatedDataPods = dataPods.map((dataPod, index) => {
      if (index === seriesIndex) {
        return { ...dataPod, active: !dataPod.active };
      }
      return dataPod;
    });

    dispatch(updateProvidedByOwnerDataPods(updatedDataPods));
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
        colors: theme.palette.mode === 'light' ? theme.palette.text.primary : theme.palette.grey[400], 
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
      enabled: true,
      enabledOnSeries: undefined,
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
        title="Your Data Pods Statistics"
        subheader={
          <span>
            Total{" "}
            <span
              style={{ fontWeight: "bold", color: theme.palette.primary.main }}
            >
              {totalTokensProvided}
            </span>{" "}
            Tokens Provided to others!
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

export default ProvidedByOwnerDataPodsStats;
