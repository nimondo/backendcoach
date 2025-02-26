import { ISpecialiteExpertise } from 'app/shared/model/specialite-expertise.model';
import { ICoachExpert } from 'app/shared/model/coach-expert.model';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';
import { TypeSeance } from 'app/shared/model/enumerations/type-seance.model';

export interface IOffreCoach {
  id?: number;
  canalSeance?: keyof typeof CanalSeance | null;
  typeSeance?: keyof typeof TypeSeance | null;
  synthese?: string | null;
  descriptionDetaillee?: string | null;
  tarif?: number;
  duree?: number;
  descriptionDiplome?: string | null;
  specialite?: ISpecialiteExpertise | null;
  coachExpert?: ICoachExpert | null;
}

export const defaultValue: Readonly<IOffreCoach> = {};
