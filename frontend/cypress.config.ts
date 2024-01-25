import { defineConfig } from 'cypress';
import axios from 'axios';

export default defineConfig({
  e2e: {
    setupNodeEvents(on) {
      on('task', {
        async clearDb() {
          await axios.get('http://localhost:8080/reset-db');
          return null;
        },
        async saveEvent() {
          await axios.post('http://localhost:8080/api/events/save', {
            name: 'Sunnipaev',
            timestamp: '2024-01-22T17:01',
            place: 'Mustamae tee 11',
            info: ''
          });
          return null;
        }
      });
    },
    viewportHeight: 1080,
    viewportWidth: 1920,
  },
  component: {
    devServer: {
      framework: 'vue',
      bundler: 'vite',
    },
    video: false
  },
});
