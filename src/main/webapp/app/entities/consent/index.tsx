import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Consent from './consent';
import ConsentDetail from './consent-detail';
import ConsentUpdate from './consent-update';
import ConsentDeleteDialog from './consent-delete-dialog';

const ConsentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Consent />} />
    <Route path="new" element={<ConsentUpdate />} />
    <Route path=":id">
      <Route index element={<ConsentDetail />} />
      <Route path="edit" element={<ConsentUpdate />} />
      <Route path="delete" element={<ConsentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ConsentRoutes;
