import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Facture from './facture';
import FactureDetail from './facture-detail';
import FactureUpdate from './facture-update';
import FactureDeleteDialog from './facture-delete-dialog';

const FactureRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Facture />} />
    <Route path="new" element={<FactureUpdate />} />
    <Route path=":id">
      <Route index element={<FactureDetail />} />
      <Route path="edit" element={<FactureUpdate />} />
      <Route path="delete" element={<FactureDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FactureRoutes;
