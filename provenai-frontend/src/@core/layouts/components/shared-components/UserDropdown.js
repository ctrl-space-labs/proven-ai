// ** React Imports
import {useState, Fragment, useMemo} from "react";

// ** Next Import
import { useRouter } from 'next/router'

// ** MUI Imports
import Box from '@mui/material/Box'
import Menu from '@mui/material/Menu'
import Badge from '@mui/material/Badge'
import Avatar from '@mui/material/Avatar'
import Divider from '@mui/material/Divider'
import MenuItem from '@mui/material/MenuItem'
import { styled } from '@mui/material/styles'
import Typography from '@mui/material/Typography'


// ** Context
import { useAuth } from "src/authentication/useAuth";
import {localStorageConstants} from "src/utils/generalConstants";
import Icon from "src/views/custom-components/mui/icon/icon";
import {generateIdenticon} from "src/utils/identiconUtil";
import apiRequests from "src/configs/apiRequest.js";
import CustomAvatar from "../../../../views/custom-components/mui/avatar";


// ** Styled Components
const BadgeContentSpan = styled('span')(({ theme }) => ({
  width: 8,
  height: 8,
  borderRadius: '50%',
  backgroundColor: theme.palette.success.main,
  boxShadow: `0 0 0 2px ${theme.palette.background.paper}`
}))

const UserDropdown = () => {

  const auth = useAuth();
  const [anchorEl, setAnchorEl] = useState(null);

  // ** Hooks
  const router = useRouter()
  let { organizationId } = router.query;

  if (!organizationId) {
    organizationId = window.localStorage.getItem(
      localStorageConstants.selectedOrganizationId
    );
  }

  const { logout } = useAuth();

  const identiconSrc = useMemo(() => generateIdenticon(auth.user.id), [auth.user.email]);


  const handleDropdownOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleDropdownClose = (url) => {
    if (url) {
      router.push(url);
    }
    setAnchorEl(null);
  };

  const handleOpenInNewTab = (url) => {
    window.open(url, '_blank');
    setAnchorEl(null);
  };

  const styles = {
    py: 2,
    px: 4,
    width: '100%',
    display: 'flex',
    alignItems: 'center',
    color: 'text.primary',
    textDecoration: 'none',
    '& svg': {
      fontSize: '1.375rem',
      color: 'text.secondary'
    }
  }

  const handleLogout = () => {
    logout();
    handleDropdownClose();
  };

  return (
    <Fragment>
      <Badge
        overlap='circular'
        onClick={handleDropdownOpen}
        sx={{ ml: 2, cursor: 'pointer' }}
        badgeContent={<BadgeContentSpan />}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Avatar
          alt="John Doe"
          onClick={handleDropdownOpen}
          sx={{ width: 40, height: 40 }}
          src={identiconSrc}
        />
      </Badge>
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={() => handleDropdownClose()}
        sx={{ '& .MuiMenu-paper': { width: 230, marginTop: 4 } }}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <Box sx={{ pt: 2, pb: 3, px: 4 }}>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <Badge
              overlap='circular'
              badgeContent={<BadgeContentSpan />}
              anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
              <Avatar
                alt={auth.user.name}
                src={identiconSrc}
                sx={{ width: "2.5rem", height: "2.5rem" }}
              />
            </Badge>
            <Box sx={{ display: 'flex', marginLeft: 3, alignItems: 'flex-start', flexDirection: 'column' }}>
              <Typography sx={{ fontWeight: 600 }}>{auth.user.name}</Typography>
              <Typography variant='body2' sx={{ fontSize: '0.8rem', color: 'text.disabled' }}>
                {auth.user.role}
              </Typography>
            </Box>
          </Box>
        </Box>
        <Divider sx={{ mt: 0, mb: 1 }} />
        <MenuItem
          sx={{ p: 0 }}
          onClick={() => {
            const gendoxUrl = `${apiRequests.gendoxFrontendUrl}/?organizationId=${organizationId}`;
            console.log(gendoxUrl);
            handleOpenInNewTab(gendoxUrl);
          }}

        >
          <Box sx={styles}>
            <Icon icon="mdi:arrow-up-bold-hexagon-outline" />
            Go to Gendox
          </Box>
        </MenuItem>
        <Divider />

        <MenuItem
          onClick={handleLogout}
          sx={{
            py: 2,
            "& svg": { mr: 2, fontSize: "1.375rem", color: "text.primary" },
          }}
        >
          <Icon icon="mdi:logout-variant" />
          Logout
        </MenuItem>
      </Menu>
    </Fragment>
  )
}

export default UserDropdown
