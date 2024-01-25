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

export enum ParticipantType {
  COMPANY = 'COMPANY',
  PERSON = 'PERSON'
}

export interface CompanyDto {
  name: string
  registryCode: string
  participantsCount: number | undefined
  paymentType: PaymentType | undefined
  info: string
}

export interface PersonDto {
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

export interface EventParticipantDto {
  id: number
  fullName: string
  code: string
  participantType: ParticipantType
}

export interface EventDetailsDto {
  id: number | undefined
  name: string
  datetime: string
  place: string
  participants: EventParticipantDto[]
}
