import client from 'app/entities/client/client.reducer';
import themeExpertise from 'app/entities/theme-expertise/theme-expertise.reducer';
import diplome from 'app/entities/diplome/diplome.reducer';
import specialiteExpertise from 'app/entities/specialite-expertise/specialite-expertise.reducer';
import coachExpert from 'app/entities/coach-expert/coach-expert.reducer';
import seanceReservationCoach from 'app/entities/seance-reservation-coach/seance-reservation-coach.reducer';
import facture from 'app/entities/facture/facture.reducer';
import paiement from 'app/entities/paiement/paiement.reducer';
import avisClient from 'app/entities/avis-client/avis-client.reducer';
import disponibilite from 'app/entities/disponibilite/disponibilite.reducer';
import offreCoach from 'app/entities/offre-coach/offre-coach.reducer';
import offreCoachMedia from 'app/entities/offre-coach-media/offre-coach-media.reducer';
import consent from 'app/entities/consent/consent.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  client,
  themeExpertise,
  diplome,
  specialiteExpertise,
  coachExpert,
  seanceReservationCoach,
  facture,
  paiement,
  avisClient,
  disponibilite,
  offreCoach,
  offreCoachMedia,
  consent,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
