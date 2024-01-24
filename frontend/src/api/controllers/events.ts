import type { AxiosResponse } from 'axios';
import apiClient from '../client';
import type { EventDto, SaveEventDto } from '@/api/types';

export default {
  futureEvents(): Promise<AxiosResponse<EventDto[]>> {
    return apiClient().get('/events/future');
  },

  pastEvents(): Promise<AxiosResponse<EventDto[]>> {
    return apiClient().get('/events/past');
  },

  saveEvent(event: SaveEventDto): Promise<AxiosResponse<void>> {
    return apiClient().post('/events/save', event);
  },

  deleteEvent(id: number): Promise<AxiosResponse<void>> {
    return apiClient().delete(`/events/${id}`);
  }
};
