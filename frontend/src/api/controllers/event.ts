import type { AxiosResponse } from 'axios';
import apiClient from '../client';
import type { EventDetailsDto, EventDto, SaveEventDto } from '@/api/types';

export default {
  futureEvents(): Promise<AxiosResponse<EventDto[]>> {
    return apiClient().get('/events/future');
  },

  pastEvents(): Promise<AxiosResponse<EventDto[]>> {
    return apiClient().get('/events/past');
  },

  eventDetails(id: number): Promise<AxiosResponse<EventDetailsDto>> {
    return apiClient().get(`/events/${id}`);
  },

  saveEvent(event: SaveEventDto): Promise<AxiosResponse<void>> {
    return apiClient().post('/events/save', event);
  },

  deleteEvent(id: number): Promise<AxiosResponse<void>> {
    return apiClient().delete(`/events/${id}`);
  },

  removePersonFromEvent(id: number, personId: number): Promise<AxiosResponse<void>> {
    return apiClient().delete(`/events/${id}/person/${personId}`);
  },

  removeCompanyFromEvent(id: number, companyId: number): Promise<AxiosResponse<void>> {
    return apiClient().delete(`/events/${id}/company/${companyId}`);
  }
};
