import PublicOnlyRoute from "src/authentication/components/PublicOnlyRoute";
import SharedAccessRoute from "src/authentication/components/SharedAccessRoute";
import PrivateRoute from "src/authentication/components/PrivateRoute";
import ProvenAiPageLoader from "src/authentication/components/ProvenAiPageLoader";
import {useEffect} from "react";
import {useAuth} from "src/authentication/useAuth";
import {useRouter} from "next/router";

import {localStorageConstants} from "src/utils/generalConstants";

const routeTypes = {
  publicOnly: 'publicOnly',
  sharedRoute: 'sharedRoute',
  private: 'private'
}

/**
 * 'RouteHandler' component is a wrapper for the different types of routes in the application.
 * @param children
 * @param routeType | 'publicOnly', 'sharedRoute', 'private'
 * @return {JSX.Element}
 * @constructor
 */
const RouteHandler = ({ children, routeType }) => {

  const auth = useAuth()
  const router = useRouter()

  useEffect(() => {
    if (auth.user && router.route === '/') {
      const homeRoute = getHomeRoute()
      router.replace(homeRoute)
    }
  }, [auth.user, router])


  let RouteTag ;
  if (routeType === routeTypes.publicOnly) {
    RouteTag = PublicOnlyRoute
  } else if (routeType === routeTypes.sharedRoute) {
    RouteTag = SharedAccessRoute
  } else {
    RouteTag = PrivateRoute
  }

  return <RouteTag pageLoader={<ProvenAiPageLoader />}>{children}</RouteTag>
}

const getHomeRoute = () => {
  let selectedOrganizationId = null


  // Check if window object is defined (client-side)
  if (typeof window !== 'undefined') {
    selectedOrganizationId = window.localStorage.getItem(localStorageConstants.selectedOrganizationId)
  }
  // Check if selectedOrganizationId and selectedProjectId are not null before constructing the URL
  if (selectedOrganizationId !== null) {
    return `/provenAI/home?organizationId=${selectedOrganizationId}`
  } else {
    // Return a default URL if selectedOrganizationId or selectedProjectId is null
    return '/provenAI/home'
  }

}

export default RouteHandler


export {routeTypes}
