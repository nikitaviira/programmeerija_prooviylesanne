export interface SaveEventDto {
  name: string
  timestamp: string
  place: string
  info: string
}

export interface EventDto {
  id: number
  name: string
  date: string
}

export enum PaymentType {
  CASH = 'CASH',
  BANK_TRANSFER = 'BANK_TRANSFER'
}

export interface CompanyDto {
  id: number | null
  name: string
  registryCode: string
  participantsCount: number | undefined
  paymentType: PaymentType | undefined
  info: string
}

export interface PersonDto {
  id: number | null
  firstName: string
  lastName: string
  personalCode: string
  paymentType: PaymentType | undefined
  info: string
}

export interface ErrorDto {
  message: string;
  fields: string[];
}
