import dayjs from 'dayjs';

export interface IEmployee {
  id?: number;
  empName?: string | null;
  empBirthday?: string | null;
  empCode?: string | null;
  empGender?: boolean | null;
  empEmail?: string | null;
  empJoinDate?: string | null;
}

export const defaultValue: Readonly<IEmployee> = {
  empGender: false,
};
