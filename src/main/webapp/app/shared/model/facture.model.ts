import dayjs from 'dayjs';
import { IPaiement } from 'app/shared/model/paiement.model';
import { TypeFacture } from 'app/shared/model/enumerations/type-facture.model';

export interface IFacture {
  id?: number;
  typeFacture?: keyof typeof TypeFacture | null;
  dateComptableFacture?: dayjs.Dayjs;
  montant?: number;
  tva?: number;
  paiement?: IPaiement | null;
}

export const defaultValue: Readonly<IFacture> = {};
