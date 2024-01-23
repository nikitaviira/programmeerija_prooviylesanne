import '@/assets/scss/global.scss';
import 'bootstrap';
import Vue3Toastify, { type ToastContainerOptions } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import {createApp} from 'vue';

import App from './App.vue';
import router from './router';

const app = createApp(App);

app.use(router);
app.use(Vue3Toastify, {
  autoClose: 2000,
  toastClassName: 'custom-toast',
  bodyClassName: 'custom-toast',
  closeButton: false,
  transition: 'slide',
  dangerouslyHTMLString: true,
  progressStyle: { background: 'white' },
  limit: 2
} as ToastContainerOptions);

app.mount('#app');
