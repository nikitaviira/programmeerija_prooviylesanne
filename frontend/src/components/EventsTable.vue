<template>
  <div class="col-md-6">
    <h5 class="table-title mb-0 text-center p-3 fw-normal">
      {{ title }}
    </h5>
    <div class="bg-white p-3">
      <div
        v-if="events.length > 0"
        class="grid"
      >
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
              @click="toParticipantsPage(event.id)"
            >
            <img
              v-if="showDelete"
              class="action-icon"
              src="@/assets/images/remove.svg"
              @click="deleteEvent(event.id)"
            >
          </div>
        </template>
      </div>
      <p
        v-else
        class="text-center"
      >
        Ei ole ühtegi üritust
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { EventDto } from '@/api/types';
  import eventsApi from '@/api/controllers/events';
  import { useRouter } from 'vue-router';

  withDefaults(defineProps<{
    title: string,
    events: EventDto[],
    showDelete?: boolean
  }>(), {
    showDelete: false
  });

  const router = useRouter();
  const emit = defineEmits<{ (e: 'event-deleted'): void }>();

  async function deleteEvent(id: number) {
    await eventsApi.deleteEvent(id);
    emit('event-deleted');
  }

  async function toParticipantsPage(id: number) {
    await router.push(`/event/${id}/participants`);
  }
</script>

<style scoped lang="scss">
  @import "@/assets/scss/variables";

  .grid {
    font-size: 14px;
    gap: 10px;
  }

  .table-title {
    background: $secondary-color;
    color: white;
  }
</style>
