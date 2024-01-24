<template>
  <span class="float-end mt-1">{{ length }} / {{ maxLength }}</span>
</template>

<script setup lang="ts">
  defineProps<{
    length: number,
    maxLength: number
  }>();
</script>
