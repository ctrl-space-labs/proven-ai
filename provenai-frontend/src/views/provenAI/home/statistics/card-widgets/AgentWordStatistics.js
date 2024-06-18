// ** MUI Imports
import Box from '@mui/material/Box'
import Card from '@mui/material/Card'
import Avatar from '@mui/material/Avatar'
import Typography from '@mui/material/Typography'
import CardHeader from '@mui/material/CardHeader'
import CardContent from '@mui/material/CardContent'

// ** Custom Components
import CustomChip from 'src/@core/components/mui/chip'
import OptionsMenu from 'src/@core/components/option-menu'
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";

const data = [
  {
    imgWidth: 22,
    imgHeight: 22,
    chipText: '$6,500',
    title: '3D Illustration',
    imgAlt: '3d-illustration',
    subtitle: 'Blender Illustration',
    src: '/images/avatars/1.png'
  },
  {
    imgWidth: 33,
    imgHeight: 22,
    chipText: '$4,290',
    subtitle: 'Figma UI Kit',
    title: 'Finance App Design',
    imgAlt: 'finance-app-design',
    src: '/images/avatars/1.png'
  },
  {
    imgWidth: 20,
    imgHeight: 22,
    title: '4 Square',
    imgAlt: '4-square',
    chipText: '$44,500',
    subtitle: 'Android Application',
    src: '/images/avatars/1.png'
  },
  {
    imgWidth: 19,
    imgHeight: 22,
    chipText: '$12,690',
    title: 'Delta Web App',
    imgAlt: 'delta-web-app',
    subtitle: 'React Dashboard',
    src: '/images/avatars/1.png'
  },
  {
    imgWidth: 23,
    imgHeight: 22,
    chipText: '$10,850',
    subtitle: 'Vue + Laravel',
    title: 'eCommerce Website',
    imgAlt: 'ecommerce-website',
    src: '/images/avatars/1.png'
  }
]

const AgentWordStatistics = () => {

    const [tokensPerOwnerDataPod, setTokensPerOwnerDataPod] = useState([]);
    const [dataPods, setDataPods] = useState([]);
    const [graphData, setGraphData] = useState([]);

    // import permissionOfUseAnalytics redux state here
    const permissionOfUseAnalytics = useSelector((state) => state.permissionOfUseAnalytics);

    useEffect(() => {

        if (!permissionOfUseAnalytics.graphData) {
            return;
        }

        const transformedData = Object.entries(permissionOfUseAnalytics.graphData.providedDataTokensByProcessorAgent).map(([key, value]) => ({
            imgWidth: 22,
            imgHeight: 22,
            chipText: `${value.totalSumTokens}`,
            title: key,
            imgAlt: 'image-alt',
            subtitle: 'some-subtitle',
            src: '/images/avatars/1.png'
        }));

        setGraphData(transformedData)

    }, [permissionOfUseAnalytics.graphData]);

  return (
    <Card>
      <CardHeader
        title='You provided to other Agents'
        action={
          <OptionsMenu
            options={['Refresh', 'Edit', 'Share']}
            iconButtonProps={{ size: 'small', className: 'card-more-options' }}
          />
        }
      />
      <CardContent>
        <Box sx={{ mb: 5, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Typography
            sx={{
              lineHeight: 2,
              fontWeight: 500,
              fontSize: '0.75rem',
              letterSpacing: '0.17px',
              textTransform: 'uppercase'
            }}
          >
            Name
          </Typography>
          <Typography
            sx={{
              lineHeight: 2,
              fontWeight: 500,
              fontSize: '0.75rem',
              letterSpacing: '0.17px',
              textTransform: 'uppercase'
            }}
          >
            Budget
          </Typography>
        </Box>
        {graphData.map((item, index) => {
          return (
            <Box
              key={item.title}
              sx={{
                display: 'flex',
                alignItems: 'center',
                mb: index !== data.length - 1 ? 5.5 : undefined
              }}
            >
              <Avatar variant='rounded' sx={{ mr: 3, width: 50, height: 42, backgroundColor: 'background.default' }}>
                <img alt='avatar' src={item.src} width={item.imgWidth} height={item.imgHeight} />
              </Avatar>
              <Box
                sx={{
                  width: '100%',
                  display: 'flex',
                  flexWrap: 'wrap',
                  alignItems: 'center',
                  justifyContent: 'space-between'
                }}
              >
                <Box sx={{ mr: 2, display: 'flex', mb: 0.4, flexDirection: 'column' }}>
                  <Typography variant='body2' sx={{ mb: 0.5, fontWeight: 600, color: 'text.primary' }}>
                    {item.title}
                  </Typography>
                  <Typography variant='caption'>{item.subtitle}</Typography>
                </Box>
                <CustomChip
                  skin='light'
                  size='small'
                  color='primary'
                  label={`${item.chipText} Tokens`}
                  sx={{ height: 20, fontSize: '0.75rem', fontWeight: 500 }}
                />
              </Box>
            </Box>
          )
        })}
      </CardContent>
    </Card>
  )
}

export default AgentWordStatistics