import { IOffreCoach } from 'app/shared/model/offre-coach.model';
import { TypeMedia } from 'app/shared/model/enumerations/type-media.model';

export interface IOffreCoachMedia {
  id?: number;
  urlMedia?: string | null;
  typeMedia?: keyof typeof TypeMedia | null;
  offreCoach?: IOffreCoach | null;
}

export const defaultValue: Readonly<IOffreCoachMedia> = {};
