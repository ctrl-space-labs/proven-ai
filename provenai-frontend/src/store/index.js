import { configureStore } from '@reduxjs/toolkit'
import logger from 'redux-logger'

import userData from 'src/store/userData/userData'
import activeProject from 'src/store/activeProject/activeProject'
import permissionOfUseAnalytics from 'src/store/permissionOfUseAnalytics/permissionOfUseAnalytics'
import userDataForAnalytics from 'src/store/userDataForAnalytics/userDataForAnalytics'
import organizationsSlice from 'src/store/organizationsSlice/organizationsSlice'
import dataPodsSlice from 'src/store/dataPodsSlice/dataPodsSlice'
import agentSlice from 'src/store/agentSlice/agentSlice'

const reducer = {
  userData,
  activeProject,
  permissionOfUseAnalytics,
  userDataForAnalytics,
  organizations: organizationsSlice, 
  dataPods: dataPodsSlice,
  agents: agentSlice,
}
export const store = configureStore({
  reducer,
  // middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(logger),
  devTools: process.env.NODE_ENV !== 'production'
})
