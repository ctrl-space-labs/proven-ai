'use client'

// React Imports
import { forwardRef } from 'react'

// MUI Imports
import MuiAvatar from '@mui/material/Avatar'
import { lighten, styled } from '@mui/material/styles'
import {hexToRGBA} from "../../../../@core/utils/hex-to-rgba";

// Got it from Materio main branch, app router version
const Avatar = styled(MuiAvatar)(({ skin, color, size, theme }) => {

  return {
    ...(color &&
      skin === 'light' && {
        color: theme.palette[color].main,
        backgroundColor: hexToRGBA(theme.palette[color].main, 0.12)
      }),
    ...(color &&
      skin === 'light-static' && {
        backgroundColor: lighten(theme.palette[color].main, 0.84),
        color: theme.palette[color].main
      }),
    ...(color &&
      skin === 'filled' && {
        color: theme.palette[color].contrastText,
        backgroundColor: theme.palette[color].main
      }),
    ...(size && {
      height: size,
      width: size
    })
  }
})

const CustomAvatar = forwardRef((props, ref) => {
  // Props
  const { color, skin = 'filled', ...rest } = props

  return <Avatar color={color} skin={skin} ref={ref} {...rest} />
})

export default CustomAvatar
