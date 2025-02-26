import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Client from './client';
import ClientDetail from './client-detail';
import ClientUpdate from './client-update';
import ClientDeleteDialog from './client-delete-dialog';

const ClientRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Client />} />
    <Route path="new" element={<ClientUpdate />} />
    <Route path=":id">
      <Route index element={<ClientDetail />} />
      <Route path="edit" element={<ClientUpdate />} />
      <Route path="delete" element={<ClientDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ClientRoutes;
