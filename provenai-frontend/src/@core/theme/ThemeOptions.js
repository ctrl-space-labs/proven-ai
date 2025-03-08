// ** MUI Theme Provider
import { deepmerge } from '@mui/utils'

// ** Theme Override Imports
import palette from './palette'
import spacing from './spacing'
import shadows from './shadows'
import breakpoints from './breakpoints'

import ProvenThemeOptions from "src/@core/theme/ProvenThemeOptions";
import GendoxThemeOptions from "src/@core/theme/ProvenThemeOptions";

const themeOptions = settings => {
  // ** Vars
  const { mode, themeColor, embeddedLayout } = settings

  const provenThemeConfig = Object.assign({}, GendoxThemeOptions(mode, themeColor, embeddedLayout))

  const themeConfig = deepmerge({
      palette: palette(mode, themeColor, embeddedLayout),
      typography: {
        fontFamily: [
          'Inter',
          'sans-serif',
          '-apple-system',
          'BlinkMacSystemFont',
          '"Segoe UI"',
          'Roboto',
          '"Helvetica Neue"',
          'Arial',
          'sans-serif',
          '"Apple Color Emoji"',
          '"Segoe UI Emoji"',
          '"Segoe UI Symbol"'
        ].join(',')
      },
      shadows: shadows(mode),
      ...spacing,
      breakpoints: breakpoints(),
      shape: {
        borderRadius: 6
      },
      mixins: {
        toolbar: {
          minHeight: 64
        }
      }
    },
    provenThemeConfig
  )

  return deepmerge(themeConfig, {
    palette: {
      primary: {
        ...themeConfig.palette[themeColor]
      }
    }
  })
}

export default themeOptions
