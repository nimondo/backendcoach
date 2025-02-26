import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { ICoachExpert } from 'app/shared/model/coach-expert.model';

export interface IAvisClient {
  id?: number;
  dateAvis?: dayjs.Dayjs;
  note?: number;
  descriptionAvis?: string | null;
  client?: IClient | null;
  coachExpert?: ICoachExpert | null;
}

export const defaultValue: Readonly<IAvisClient> = {};
