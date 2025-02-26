export interface IConsent {
  id?: number;
  email?: string;
  necessary?: boolean | null;
  analytics?: boolean | null;
  marketing?: boolean | null;
  preferences?: boolean | null;
}

export const defaultValue: Readonly<IConsent> = {
  necessary: false,
  analytics: false,
  marketing: false,
  preferences: false,
};
