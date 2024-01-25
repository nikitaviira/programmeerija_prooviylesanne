<template>
  <div class="text-center">
    <h1 class="mt-5">
      Lehekülge ei leitud
    </h1>
    <button
      class="mx-auto back-btn btn mb-5"
      @click="toHomePage"
    >
      Tagasi avalehele
    </button>
  </div>
</template>

<script setup lang="ts">
  import { useRouter } from 'vue-router';

  const router = useRouter();

  async function toHomePage() {
    await router.push('/');
  }
</script>

<style scoped lang="scss">

</style>
