<template>
  <Form title="Osavõtja info">
    <div class="form-container p-4">
      <p class="form-title fw-normal fs-3">
        Osavõtja info
      </p>
      <PersonForm @back="() => {}" />
    </div>
  </Form>
</template>

<script setup lang="ts">
  import Form from '@/components/Form.vue';
  import PersonForm from '@/components/PersonForm.vue';
  import { useRoute } from 'vue-router';

  const route = useRoute();
</script>
