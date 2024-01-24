import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import AddEventView from '@/views/AddEventView.vue';
import ParticipantsView from '@/views/ParticipantsView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/add-event',
      name: 'add-event',
      component: AddEventView
    },
    {
      path: '/participants',
      name: 'participants',
      component: ParticipantsView
    }
  ]
});

export default router;
