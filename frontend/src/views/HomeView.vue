<template>
  <div>
    <div class="card">
      <div class="row g-0 flex-md-row flex-column-reverse">
        <div class="col-md-6 left-col d-flex align-items-center">
          <div class="card-body p-4">
            <p class="card-text">
              Sed nec elit vestibulum, <b>tincidunt orci</b> et, sagittis ex.
              Vestibulum rutrum <b>neque suscipit</b> ante mattis maximus.
              Nulla non sapien <b>viverra, lobortis lorem non</b>, accumsan metus.
            </p>
          </div>
        </div>
        <div class="col-md-6 right-col">
          <img
            src="@/assets/images/pilt.jpg"
            class="img-fluid w-100"
            alt="pink"
          >
        </div>
      </div>
    </div>

    <div class="mt-3">
      <div class="row">
        <EventsTable
          class="mb-3 mb-md-0"
          title="Tulevased üritused"
          :events="futureEvents"
          show-delete
          @eventDeleted="loadEvents()"
        />
        <EventsTable
          title="Toimunud üritused"
          :events="pastEvents"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import EventsTable from '@/components/EventsTable.vue';
  import type { EventDto } from '@/api/types';
  import {onBeforeMount, ref} from "vue";
  import eventsApi from '@/api/controllers/events';

  const futureEvents = ref<EventDto[]>([]);
  const pastEvents = ref<EventDto[]>([]);

  onBeforeMount(async () => {
    await loadEvents();
  })

  async function loadEvents() {
    const [future, past] = await Promise.all([
      eventsApi.futureEvents(),
      eventsApi.pastEvents()
    ]);

    futureEvents.value = future.data;
    pastEvents.value = past.data;
  }
</script>

<style lang="scss" scoped>
  @import "@/assets/scss/variables";

  .card {
    border-radius: 0;
    border: 0;
  }

  .left-col {
    background: $secondary-color;
    color: white;
  }

  .card-text {
    font-size: clamp(1rem, 1.5vw, 1.3rem);
  }
</style>
