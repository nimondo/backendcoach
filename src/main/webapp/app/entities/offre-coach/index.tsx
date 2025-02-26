import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OffreCoach from './offre-coach';
import OffreCoachDetail from './offre-coach-detail';
import OffreCoachUpdate from './offre-coach-update';
import OffreCoachDeleteDialog from './offre-coach-delete-dialog';

const OffreCoachRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OffreCoach />} />
    <Route path="new" element={<OffreCoachUpdate />} />
    <Route path=":id">
      <Route index element={<OffreCoachDetail />} />
      <Route path="edit" element={<OffreCoachUpdate />} />
      <Route path="delete" element={<OffreCoachDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OffreCoachRoutes;
