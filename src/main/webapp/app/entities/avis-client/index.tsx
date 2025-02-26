import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AvisClient from './avis-client';
import AvisClientDetail from './avis-client-detail';
import AvisClientUpdate from './avis-client-update';
import AvisClientDeleteDialog from './avis-client-delete-dialog';

const AvisClientRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AvisClient />} />
    <Route path="new" element={<AvisClientUpdate />} />
    <Route path=":id">
      <Route index element={<AvisClientDetail />} />
      <Route path="edit" element={<AvisClientUpdate />} />
      <Route path="delete" element={<AvisClientDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AvisClientRoutes;
