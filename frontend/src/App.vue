<template>
  <div class="container px-0 main">
    <NavBar />
    <RouterView />
    <Footer />
  </div>
</template>

<script setup lang="ts">
  import NavBar from '@/components/NavBar.vue';
  import Footer from '@/components/Footer.vue';
</script>
