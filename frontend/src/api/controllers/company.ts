import type { AxiosResponse } from 'axios';
import apiClient from '../client';
import type { CompanyDto } from '@/api/types';

export default {
  company(id: number): Promise<AxiosResponse<CompanyDto>> {
    return apiClient().get(`/company/${id}`);
  },

  saveCompany(eventId: number, company: CompanyDto): Promise<AxiosResponse<void>> {
    return apiClient().post('/company/save', company, { params: { eventId } });
  },

  updateCompany(id: number, company: CompanyDto): Promise<AxiosResponse<void>> {
    return apiClient().put(`/company/${id}/update`, company);
  }
};
