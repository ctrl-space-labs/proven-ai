// ** MUI Imports
import Card from '@mui/material/Card'
import { useTheme } from '@mui/material/styles'
import CardHeader from '@mui/material/CardHeader'
import CardContent from '@mui/material/CardContent'

// ** Custom Components Imports
import OptionsMenu from 'src/@core/components/option-menu'
import ReactApexcharts from 'src/@core/components/react-apexcharts'

// ** Util Import
import { hexToRGBA } from 'src/@core/utils/hex-to-rgba'
import {useEffect} from "react";
import {useState} from "react";
import {useSelector} from "react-redux";



const DataPodsStats = () => {
  // ** Hook
  const theme = useTheme()

  const [tokensPerOwnerDataPod, setTokensPerOwnerDataPod] = useState([]);
  const [dataPods, setDataPods] = useState([]);
  const [totalTokensProvided, setTotalTokensProvided] = useState(0);

  // import permissionOfUseAnalytics redux state here
  const permissionOfUseAnalytics = useSelector((state) => state.permissionOfUseAnalytics);

  useEffect(() => {

    console.log("graph data updated! ", permissionOfUseAnalytics.graphData);
    if (!permissionOfUseAnalytics.graphData) {
      return;
    }

    const dataPodsStatsData = [
      {
        name: 'Sales',
        data: Object.values(permissionOfUseAnalytics.graphData.providedDataTokensByOwnerDataPod).map((item) => item.totalSumTokens)
      }
    ];

    const totalTokens = dataPodsStatsData[0].data.reduce((acc, item) => acc + item, 0);

    console.log("totalTokens: ", totalTokens);

    setTotalTokensProvided(totalTokens);


    const categories = Object.keys(permissionOfUseAnalytics.graphData.providedDataTokensByOwnerDataPod);


    setTokensPerOwnerDataPod(dataPodsStatsData);

    setDataPods(categories);

    console.log("categories: ", categories);
    console.log("series: ", dataPodsStatsData);

  }, [permissionOfUseAnalytics.graphData]);

  const options = {
    chart: {
      parentHeightOffset: 0,
      toolbar: { show: false }
    },
    plotOptions: {
      bar: {
        borderRadius: 8,
        barHeight: '60%',
        horizontal: true,
        distributed: true,
        startingShape: 'rounded'
      }
    },
    dataLabels: {
      offsetY: 8,
      style: {
        fontWeight: 500,
        fontSize: '0.875rem'
      }
    },
    grid: {
      strokeDashArray: 8,
      borderColor: theme.palette.divider,
      xaxis: {
        lines: { show: true }
      },
      yaxis: {
        lines: { show: false }
      },
      padding: {
        top: -18,
        left: 21,
        right: 33,
        bottom: 10
      }
    },
    colors: [
      hexToRGBA(theme.palette.primary.light, 1),
      hexToRGBA(theme.palette.success.light, 1),
      hexToRGBA(theme.palette.warning.light, 1),
      hexToRGBA(theme.palette.info.light, 1),
      hexToRGBA(theme.palette.error.light, 1)
    ],
    legend: { show: false },
    states: {
      hover: {
        filter: { type: 'none' }
      },
      active: {
        filter: { type: 'none' }
      }
    },
    xaxis: {
      axisTicks: { show: false },
      axisBorder: { show: false },
      categories: dataPods,
      labels: {
        formatter: val => `${Number(val) / 1000}k`,
        style: {
          fontSize: '0.875rem',
          colors: theme.palette.text.disabled
        }
      }
    },
    yaxis: {
      labels: {
        align: theme.direction === 'rtl' ? 'right' : 'left',
        style: {
          fontWeight: 600,
          fontSize: '0.875rem',
          colors: theme.palette.text.primary
        }
      }
    }
  }

  return (
    <Card>
      <CardHeader
        title='Your Data Pods Statistics'
        subheader={`Total ${totalTokensProvided} Tokens Provided to others!`}
        subheaderTypographyProps={{ sx: { lineHeight: 1.429 } }}
        titleTypographyProps={{ sx: { letterSpacing: '0.15px' } }}
        action={
          <OptionsMenu
            options={['Last 28 Days', 'Last Month', 'Last Year']}
            iconButtonProps={{ size: 'small', className: 'card-more-options' }}
          />
        }
      />
      <CardContent sx={{ p: '0 !important' }}>
        <ReactApexcharts type='bar' height={294} series={tokensPerOwnerDataPod} options={options} />
      </CardContent>
    </Card>
  )
}

export default DataPodsStats
