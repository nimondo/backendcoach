import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CoachExpert from './coach-expert';
import CoachExpertDetail from './coach-expert-detail';
import CoachExpertUpdate from './coach-expert-update';
import CoachExpertDeleteDialog from './coach-expert-delete-dialog';

const CoachExpertRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CoachExpert />} />
    <Route path="new" element={<CoachExpertUpdate />} />
    <Route path=":id">
      <Route index element={<CoachExpertDetail />} />
      <Route path="edit" element={<CoachExpertUpdate />} />
      <Route path="delete" element={<CoachExpertDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CoachExpertRoutes;
