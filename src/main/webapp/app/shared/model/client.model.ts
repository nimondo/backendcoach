import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { GenrePersonne } from 'app/shared/model/enumerations/genre-personne.model';
import { CanalSeance } from 'app/shared/model/enumerations/canal-seance.model';

export interface IClient {
  id?: number;
  genre?: keyof typeof GenrePersonne;
  nom?: string;
  prenom?: string;
  dateNaissance?: dayjs.Dayjs;
  adresseEmail?: string;
  numeroTelephone?: string | null;
  ville?: string;
  codePostal?: number;
  numeroEtNomVoie?: string;
  preferenceCanalSeance?: keyof typeof CanalSeance | null;
  photoContentType?: string | null;
  photo?: string | null;
  urlPhotoProfil?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IClient> = {};
