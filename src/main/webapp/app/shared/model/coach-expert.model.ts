import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ISpecialiteExpertise } from 'app/shared/model/specialite-expertise.model';
import { IDiplome } from 'app/shared/model/diplome.model';
import { GenrePersonne } from 'app/shared/model/enumerations/genre-personne.model';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';

export interface ICoachExpert {
  id?: number;
  civilite?: keyof typeof GenrePersonne;
  nom?: string;
  prenom?: string;
  dateNaissance?: dayjs.Dayjs;
  adresseEmail?: string;
  numeroTelephone?: string | null;
  ville?: string;
  codePostal?: number;
  numeroEtNomVoie?: string;
  tarifActuel?: number;
  formatProposeSeance?: keyof typeof CanalSeance;
  photoContentType?: string | null;
  photo?: string | null;
  urlPhotoProfil?: string;
  bio?: string | null;
  user?: IUser | null;
  specialiteExpertises?: ISpecialiteExpertise[] | null;
  diplomes?: IDiplome[] | null;
}

export const defaultValue: Readonly<ICoachExpert> = {};
