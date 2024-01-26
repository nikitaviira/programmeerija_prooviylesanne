import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    setupNodeEvents(on) {
      on('task', {
        async clearDb() {
          await fetch('http://localhost:8080/reset-db');
          return null;
        },
        async saveEvent() {
          await fetch('http://localhost:8080/create-mock-event');
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
