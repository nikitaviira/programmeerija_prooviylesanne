<template>
  <div class="form">
    <FormHeader :title="title" />
    <div class="row g-0 w-100 bg-white d-flex justify-content-center">
      <slot/>
    </div>
  </div>
</template>

<script setup lang="ts">
  import FormHeader from '@/components/FormHeader.vue';

  defineProps<{ title: string }>();
</script>

<style lang="scss" scoped>
  @import "@/assets/scss/variables";
</style>
