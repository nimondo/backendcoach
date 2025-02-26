import dayjs from 'dayjs';
import { ICoachExpert } from 'app/shared/model/coach-expert.model';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';

export interface IDisponibilite {
  id?: number;
  heureDebutCreneauxDisponibilite?: dayjs.Dayjs;
  heureFinCreneauxDisponibilite?: dayjs.Dayjs;
  canalSeance?: keyof typeof CanalSeance | null;
  coachExpert?: ICoachExpert | null;
}

export const defaultValue: Readonly<IDisponibilite> = {};
