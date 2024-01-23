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

export interface ErrorDto {
  message: string;
  fields: string[];
}
