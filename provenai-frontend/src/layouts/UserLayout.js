import useMediaQuery from "@mui/material/useMediaQuery";
import { styled, useTheme } from "@mui/material/styles";
import { useRouter } from "next/router";

// ** Layout Imports
// !Do not remove this Layout import
import Layout from "src/@core/layouts/Layout";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Icon from "src/@core/components/icon";
import Button from "@mui/material/Button";
import Link from "next/link";

// ** Navigation Imports
import VerticalNavItems from "src/navigation/vertical";
import HorizontalNavItems from "src/navigation/horizontal";

import VerticalAppBarContent from "./components/vertical/AppBarContent";
import HorizontalAppBarContent from "./components/horizontal/AppBarContent";

// ** Hook Import
import { useSettings } from "src/@core/hooks/useSettings";

import { be } from "date-fns/locale";


const UserLayout = ({ children, contentHeightFixed }) => {
  const router = useRouter();

  // ** Hooks
  const { settings, saveSettings } = useSettings();

  // ** Vars for server side navigation
  // const { menuItems: verticalMenuItems } = ServerSideVerticalNavItems()
  // const { menuItems: horizontalMenuItems } = ServerSideHorizontalNavItems()
  /**
   *  The below variable will hide the current layout menu at given screen size.
   *  The menu will be accessible from the Hamburger icon only (Vertical Overlay Menu).
   *  You can change the screen size from which you want to hide the current layout menu.
   *  Please refer useMediaQuery() hook: https://mui.com/material-ui/react-use-media-query/,
   *  to know more about what values can be passed to this hook.
   *  ! Do not change this value unless you know what you are doing. It can break the template.
   */
  const hidden = useMediaQuery((theme) => theme.breakpoints.down("lg"));
  if (hidden && settings.layout === "horizontal") {
    settings.layout = "vertical";
  }

  

  const AppBrand = () => {
    return (
      <Link href="/provenAI/home" passHref style={{ textDecoration: 'none' }}>
        <Box
          
          sx={{
            display: "flex",
            alignItems: "center",
            cursor: "pointer",
            padding: "20px 20px",    
          }}
          
        >
          <div
            style={{
              width: "40px",
              height: "40px",
              backgroundImage: "url('/images/provenAILogo.svg')",
              backgroundSize: "30px 30px",
              backgroundRepeat: "no-repeat",
              backgroundPosition: "center",
            }}
          />
          <Typography
            variant="h5"
            sx={{
              ml: 2,              
            }}
          >
            ProvenAI
          </Typography>
        </Box>
      </Link>
    );
  };

  return (
    <Layout
      hidden={hidden}
      settings={settings}
      saveSettings={saveSettings}
      contentHeightFixed={contentHeightFixed}
      verticalLayoutProps={{
        navMenu: {
          branding: () => <AppBrand />, 
          navItems: VerticalNavItems(),               
        },

        appBar: {
          content: (props) => (
            <VerticalAppBarContent
              hidden={hidden}
              settings={settings}
              saveSettings={saveSettings}
              toggleNavVisibility={props.toggleNavVisibility}
            />
          ),
        },
      }}
      {...(settings.layout === "horizontal" && {
        horizontalLayoutProps: {
          navMenu: {
            navItems: HorizontalNavItems(),
            
            // Uncomment the below line when using server-side menu in horizontal layout and comment the above line
            // navItems: horizontalMenuItems
          },
          appBar: {
            content: () => (
              <HorizontalAppBarContent
                settings={settings}
                saveSettings={saveSettings}
              />
            ),
          },
        },
      })}
    >
      {children}
    </Layout>
  );
};

export default UserLayout;
