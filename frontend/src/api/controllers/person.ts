import type { AxiosResponse } from 'axios';
import apiClient from '../client';
import type { PersonDto } from '@/api/types';

export default {
  person(id: number): Promise<AxiosResponse<PersonDto>> {
    return apiClient().get(`/person/${id}`);
  },

  savePerson(eventId: number, person: PersonDto): Promise<AxiosResponse<void>> {
    return apiClient().post('/person/save', person, { params: { eventId } });
  },

  updatePerson(id: number, person: PersonDto): Promise<AxiosResponse<void>> {
    return apiClient().put(`/person/${id}/update`, person);
  }
};
