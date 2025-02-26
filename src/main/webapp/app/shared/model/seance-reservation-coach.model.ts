import dayjs from 'dayjs';
import { IFacture } from 'app/shared/model/facture.model';
import { ICoachExpert } from 'app/shared/model/coach-expert.model';
import { IClient } from 'app/shared/model/client.model';
import { IOffreCoach } from 'app/shared/model/offre-coach.model';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';
import { TypeSeance } from 'app/shared/model/enumerations/type-seance.model';
import { StatutSeance } from 'app/shared/model/enumerations/statut-seance.model';

export interface ISeanceReservationCoach {
  id?: number;
  heureDebutCreneauReserve?: dayjs.Dayjs;
  heureFinCreneauReserve?: dayjs.Dayjs;
  canalSeance?: keyof typeof CanalSeance;
  typeSeance?: keyof typeof TypeSeance;
  statutRealisation?: keyof typeof StatutSeance;
  facture?: IFacture | null;
  coachExpert?: ICoachExpert | null;
  client?: IClient | null;
  offre?: IOffreCoach | null;
}

export const defaultValue: Readonly<ISeanceReservationCoach> = {};
