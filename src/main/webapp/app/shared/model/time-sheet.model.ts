import dayjs from 'dayjs';

export interface ITimeSheet {
  id?: number;
  date?: string | null;
  checkIn?: string | null;
  checkOut?: string | null;
  overTime?: string | null;
  user?: string | null;
}

export const defaultValue: Readonly<ITimeSheet> = {};
