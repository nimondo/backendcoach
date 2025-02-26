import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SeanceReservationCoach from './seance-reservation-coach';
import SeanceReservationCoachDetail from './seance-reservation-coach-detail';
import SeanceReservationCoachUpdate from './seance-reservation-coach-update';
import SeanceReservationCoachDeleteDialog from './seance-reservation-coach-delete-dialog';

const SeanceReservationCoachRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SeanceReservationCoach />} />
    <Route path="new" element={<SeanceReservationCoachUpdate />} />
    <Route path=":id">
      <Route index element={<SeanceReservationCoachDetail />} />
      <Route path="edit" element={<SeanceReservationCoachUpdate />} />
      <Route path="delete" element={<SeanceReservationCoachDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SeanceReservationCoachRoutes;
