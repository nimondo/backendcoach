import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Client from './client';
import ThemeExpertise from './theme-expertise';
import Diplome from './diplome';
import SpecialiteExpertise from './specialite-expertise';
import CoachExpert from './coach-expert';
import SeanceReservationCoach from './seance-reservation-coach';
import Facture from './facture';
import Paiement from './paiement';
import AvisClient from './avis-client';
import Disponibilite from './disponibilite';
import OffreCoach from './offre-coach';
import OffreCoachMedia from './offre-coach-media';
import Consent from './consent';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="client/*" element={<Client />} />
        <Route path="theme-expertise/*" element={<ThemeExpertise />} />
        <Route path="diplome/*" element={<Diplome />} />
        <Route path="specialite-expertise/*" element={<SpecialiteExpertise />} />
        <Route path="coach-expert/*" element={<CoachExpert />} />
        <Route path="seance-reservation-coach/*" element={<SeanceReservationCoach />} />
        <Route path="facture/*" element={<Facture />} />
        <Route path="paiement/*" element={<Paiement />} />
        <Route path="avis-client/*" element={<AvisClient />} />
        <Route path="disponibilite/*" element={<Disponibilite />} />
        <Route path="offre-coach/*" element={<OffreCoach />} />
        <Route path="offre-coach-media/*" element={<OffreCoachMedia />} />
        <Route path="consent/*" element={<Consent />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
