import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Paiement from './paiement';
import PaiementDetail from './paiement-detail';
import PaiementUpdate from './paiement-update';
import PaiementDeleteDialog from './paiement-delete-dialog';

const PaiementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Paiement />} />
    <Route path="new" element={<PaiementUpdate />} />
    <Route path=":id">
      <Route index element={<PaiementDetail />} />
      <Route path="edit" element={<PaiementUpdate />} />
      <Route path="delete" element={<PaiementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaiementRoutes;
