import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SpecialiteExpertise from './specialite-expertise';
import SpecialiteExpertiseDetail from './specialite-expertise-detail';
import SpecialiteExpertiseUpdate from './specialite-expertise-update';
import SpecialiteExpertiseDeleteDialog from './specialite-expertise-delete-dialog';

const SpecialiteExpertiseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SpecialiteExpertise />} />
    <Route path="new" element={<SpecialiteExpertiseUpdate />} />
    <Route path=":id">
      <Route index element={<SpecialiteExpertiseDetail />} />
      <Route path="edit" element={<SpecialiteExpertiseUpdate />} />
      <Route path="delete" element={<SpecialiteExpertiseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SpecialiteExpertiseRoutes;
