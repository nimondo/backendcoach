import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Diplome from './diplome';
import DiplomeDetail from './diplome-detail';
import DiplomeUpdate from './diplome-update';
import DiplomeDeleteDialog from './diplome-delete-dialog';

const DiplomeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Diplome />} />
    <Route path="new" element={<DiplomeUpdate />} />
    <Route path=":id">
      <Route index element={<DiplomeDetail />} />
      <Route path="edit" element={<DiplomeUpdate />} />
      <Route path="delete" element={<DiplomeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DiplomeRoutes;
