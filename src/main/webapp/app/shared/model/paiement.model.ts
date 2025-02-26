import dayjs from 'dayjs';
import { StatutPaiement } from 'app/shared/model/enumerations/statut-paiement.model';

export interface IPaiement {
  id?: number;
  horodatage?: dayjs.Dayjs;
  moyenPaiement?: string | null;
  statutPaiement?: keyof typeof StatutPaiement | null;
  idStripe?: string | null;
}

export const defaultValue: Readonly<IPaiement> = {};
