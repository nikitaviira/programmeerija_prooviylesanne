import type { AxiosInstance } from 'axios';
import Axios from 'axios';
import { toast } from 'vue3-toastify';
import type { ErrorDto } from '@/api/types';
import router from '@/router';

export default (): AxiosInstance => {
  const instance = Axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL + '/api/'
  });

  instance.defaults.headers.common['Access-Control-Allow-Origin'] = import.meta.env.VITE_API_BASE_URL;

  instance.interceptors.response.use((x) => x, async(error) => {
    if (error.response.status === 404) {
      await router.push('/404');
    } else {
      const errorData: ErrorDto = error.response.data;
      if (error.response.data) {
        if (errorData.fields.length > 0) {
          toast(`<ul>${errorData.fields.map((error) => `<li>${error}</li>`).join('')}</ul>`);
        } else {
          toast(errorData.message);
        }
      }

      throw error;
    }
  });

  return instance;
};
