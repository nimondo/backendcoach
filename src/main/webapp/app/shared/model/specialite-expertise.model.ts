import { IDiplome } from 'app/shared/model/diplome.model';
import { IThemeExpertise } from 'app/shared/model/theme-expertise.model';
import { ICoachExpert } from 'app/shared/model/coach-expert.model';

export interface ISpecialiteExpertise {
  id?: number;
  specialite?: string;
  specialiteDescription?: string | null;
  tarifMoyenHeure?: number | null;
  dureeTarif?: string | null;
  diplomeRequis?: boolean;
  urlPhoto?: string;
  diplome?: IDiplome | null;
  themeExpertise?: IThemeExpertise | null;
  coachExperts?: ICoachExpert[] | null;
}

export const defaultValue: Readonly<ISpecialiteExpertise> = {
  diplomeRequis: false,
};
