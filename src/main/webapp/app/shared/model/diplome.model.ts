import { ICoachExpert } from 'app/shared/model/coach-expert.model';

export interface IDiplome {
  id?: number;
  libelle?: string;
  nbAnneesEtudePostBac?: number | null;
  coachExperts?: ICoachExpert[] | null;
}

export const defaultValue: Readonly<IDiplome> = {};
