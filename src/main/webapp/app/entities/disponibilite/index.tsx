import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Disponibilite from './disponibilite';
import DisponibiliteDetail from './disponibilite-detail';
import DisponibiliteUpdate from './disponibilite-update';
import DisponibiliteDeleteDialog from './disponibilite-delete-dialog';

const DisponibiliteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Disponibilite />} />
    <Route path="new" element={<DisponibiliteUpdate />} />
    <Route path=":id">
      <Route index element={<DisponibiliteDetail />} />
      <Route path="edit" element={<DisponibiliteUpdate />} />
      <Route path="delete" element={<DisponibiliteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DisponibiliteRoutes;
