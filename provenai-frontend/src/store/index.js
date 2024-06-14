// ** Toolkit imports
import { configureStore } from '@reduxjs/toolkit'

// ** Reducers

import userData from 'src/store/apps/userData/userData'
import activeOrganization from 'src/store/apps/activeOrganization/activeOrganization'
import activeProject from 'src/store/apps/activeProject/activeProject'
import activeDocument from 'src/store/apps/activeDocument/activeDocument'
import permissionOfUseAnalytics from 'src/store/apps/permissionOfUseAnalytics/permissionOfUseAnalytics'

export const store = configureStore({
  reducer: {
    userData,
    activeProject,
    activeOrganization,
    activeDocument,
    permissionOfUseAnalytics
    
  },
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false
    })
})
