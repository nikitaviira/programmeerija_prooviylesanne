<template>
  <Form title="Osavõtja info">
    <div class="form-container p-4">
      <p class="form-title fw-normal fs-3">
        Osavõtja info
      </p>
      <CompanyForm @back="() => {}" />
    </div>
  </Form>
</template>

<script setup lang="ts">
  import CompanyForm from '@/components/CompanyForm.vue';
  import Form from '@/components/Form.vue';
  import { useRoute } from 'vue-router';

  const route = useRoute();
</script>
