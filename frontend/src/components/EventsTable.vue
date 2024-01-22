<template>
  <div class="col-md-6">
    <h5 class="table-title mb-0 text-center p-3 fw-normal">
      {{ title }}
    </h5>
    <div class="bg-white p-3">
      <div class="grid">
        <template
          v-for="(event, index) in events"
          :key="index"
        >
          <div class="g-col-1">
            {{ index + 1 }}.
          </div>
          <div class="g-col-5">
            {{ event.name }}
          </div>
          <div class="g-col-3 text-center">
            {{ event.date }}
          </div>
          <div class="g-col-3 text-center">
            <img
              class="action-icon mx-2"
              src="@/assets/images/people.svg"
              alt="show members button"
            >
            <img
              v-if="showDelete"
              class="action-icon"
              src="@/assets/images/remove.svg"
              alt="remove event button"
            >
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { Event } from '@/api/types';

  withDefaults(defineProps<{
    title: string,
    events: Event[],
    showDelete?: boolean
  }>(), {
    showDelete: false
  });
</script>

<style scoped lang="scss">
  @import "@/assets/scss/variables";

  .grid {
    font-size: 14px;
    gap: 10px;
  }

  .action-icon {
    width: 25px;
    cursor: pointer;
  }

  .table-title {
    background: $secondary-color;
    color: white;
  }
</style>
