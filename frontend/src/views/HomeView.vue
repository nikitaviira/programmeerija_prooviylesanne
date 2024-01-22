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
  import type { Event } from '@/api/types';

  const futureEvents: Event[] = [
    {
      id: 1,
      name: 'Aenean commodo',
      date: '14.02.2016'
    },
    {
      id: 2,
      name: 'Fusce ex dui, finibus eu luctus vel',
      date: '12.03.2023'
    },
    {
      id: 3,
      name: 'Nunc lobortis metus eu massa viverra ultri iplacerat nibh',
      date: '12.04.2024'
    },
    {
      id: 4,
      name: 'Integer nec nulla vitae',
      date: '12.04.2024'
    }
  ];

  const pastEvents: Event[] = [
    {
      id: 1,
      name: 'Aenean commodo',
      date: '14.02.2016'
    },
    {
      id: 2,
      name: 'Fusce ex dui, finibus eu luctus vel',
      date: '12.03.2023'
    },
    {
      id: 3,
      name: 'Nunc lobortis metus eu massa viverra ultri iplacerat nibh',
      date: '12.04.2024'
    },
    {
      id: 4,
      name: 'Integer nec nulla vitae',
      date: '12.04.2024'
    },
    {
      id: 5,
      name: 'Praesent molestie dapibus lorem',
      date: '12.04.2024'
    }
  ];
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
