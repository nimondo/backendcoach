import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ThemeExpertise from './theme-expertise';
import ThemeExpertiseDetail from './theme-expertise-detail';
import ThemeExpertiseUpdate from './theme-expertise-update';
import ThemeExpertiseDeleteDialog from './theme-expertise-delete-dialog';

const ThemeExpertiseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ThemeExpertise />} />
    <Route path="new" element={<ThemeExpertiseUpdate />} />
    <Route path=":id">
      <Route index element={<ThemeExpertiseDetail />} />
      <Route path="edit" element={<ThemeExpertiseUpdate />} />
      <Route path="delete" element={<ThemeExpertiseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ThemeExpertiseRoutes;
