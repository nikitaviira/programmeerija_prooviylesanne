import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import AddEventView from '@/views/AddEventView.vue';
import ParticipantsView from '@/views/ParticipantsView.vue';
import PersonDetailsView from '@/views/PersonDetailsView.vue';
import CompanyDetailsView from '@/views/CompanyDetailsView.vue';

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
      path: '/event/:id/participants',
      name: 'participants',
      component: ParticipantsView,
      props: true
    },
    {
      path: '/company/:id',
      name: 'company-details',
      component: CompanyDetailsView,
      props: true
    },
    {
      path: '/person/:id',
      name: 'person-details',
      component: PersonDetailsView,
      props: true
    }
  ]
});

export default router;
