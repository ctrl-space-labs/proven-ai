// ** Toolkit imports
import { configureStore } from '@reduxjs/toolkit'

// ** Reducers

import userData from 'src/store/apps/userData/userData'
import activeOrganization from 'src/store/apps/activeOrganization/activeOrganization'
import activeProject from 'src/store/apps/activeProject/activeProject'
import permissionOfUseAnalytics from 'src/store/apps/permissionOfUseAnalytics/permissionOfUseAnalytics'
import userDataForAnalytics from 'src/store/apps/userDataForAnalytics/userDataForAnalytics' 

export const store = configureStore({
  reducer: {
    userData,
    activeProject,
    activeOrganization,
    permissionOfUseAnalytics,
    userDataForAnalytics
    
  },
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false
    })
})
