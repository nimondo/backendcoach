import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OffreCoachMedia from './offre-coach-media';
import OffreCoachMediaDetail from './offre-coach-media-detail';
import OffreCoachMediaUpdate from './offre-coach-media-update';
import OffreCoachMediaDeleteDialog from './offre-coach-media-delete-dialog';

const OffreCoachMediaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OffreCoachMedia />} />
    <Route path="new" element={<OffreCoachMediaUpdate />} />
    <Route path=":id">
      <Route index element={<OffreCoachMediaDetail />} />
      <Route path="edit" element={<OffreCoachMediaUpdate />} />
      <Route path="delete" element={<OffreCoachMediaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OffreCoachMediaRoutes;
