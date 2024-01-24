<template>
  <Form title="Ürituse lisamine">
    <div class="form-container p-4">
      <p class="form-title fw-normal fs-3">
        Ürituse lisamine
      </p>
      <div class="row mb-3">
        <label class="col-sm-3 col-form-label">
          Ürituse nimi:
        </label>
        <div class="col-sm-9">
          <input
            v-model.trim="eventForm.name"
            type="text"
            :class="{'is-invalid': $v.name.$error}"
            class="form-control"
            @input="$v.name.$touch()"
          >
          <div
            v-if="$v.name.$dirty && $v.name.$error"
            class="invalid-feedback d-block"
          >
            See väli on kohustuslik
          </div>
        </div>
      </div>
      <div class="row mb-3">
        <label class="col-sm-3 col-form-label">
          Toimumisaeg:
        </label>
        <div class="col-sm-9">
          <input
            v-model.trim="eventForm.timestamp"
            :class="{'is-invalid': $v.timestamp.$error}"
            type="datetime-local"
            :min="new Date().toISOString().slice(0, -8)"
            class="form-control"
            @input="$v.timestamp.$touch()"
          >
          <div
            v-if="$v.timestamp.$dirty && $v.timestamp.required.$invalid"
            class="invalid-feedback d-block"
          >
            See väli on kohustuslik
          </div>
          <div
            v-else-if="$v.timestamp.$dirty && $v.timestamp.timestampValidation.$invalid"
            class="invalid-feedback d-block"
          >
            Toimumisaeg peab olema tulevikus
          </div>
        </div>
      </div>
      <div class="row mb-3">
        <label class="col-sm-3 col-form-label">
          Koht:
        </label>
        <div class="col-sm-9">
          <input
            v-model.trim="eventForm.place"
            :class="{'is-invalid': $v.place.$error}"
            type="text"
            class="form-control"
            @input="$v.place.$touch()"
          >
          <div
            v-if="$v.place.$dirty && $v.place.$error"
            class="invalid-feedback d-block"
          >
            See väli on kohustuslik
          </div>
        </div>
      </div>
      <div class="row mb-3">
        <label class="col-sm-3 col-form-label">
          Lisainfo:
        </label>
        <div class="col-sm-9">
          <textarea
            v-model.trim="eventForm.info"
            maxlength="1000"
            class="form-control"
          />
          <WordCounter
            :length="eventForm.info.length"
            :max-length="1000"
          />
        </div>
      </div>
      <button
        type="button"
        class="btn back-btn me-2"
        @click="toHomePage"
      >
        Tagasi
      </button>
      <button
        type="button"
        class="btn submit-btn"
        @click.prevent="submitForm"
      >
        Lisa
      </button>
    </div>
  </Form>
</template>

<script setup lang="ts">
  import Form from '@/components/Form.vue';
  import { ref } from 'vue';
  import useVuelidate from '@vuelidate/core';
  import { required } from '@vuelidate/validators';
  import { useRouter } from 'vue-router';
  import eventsApi from '@/api/controllers/events';
  import type { SaveEventDto } from '@/api/types';
  import WordCounter from '@/components/WordCounter.vue';

  const router = useRouter();
  const timestampValidation = (value: string) => new Date() <= new Date(value);
  const rules = {
    name: { required },
    timestamp: { required, timestampValidation },
    place: { required }
  };

  const eventForm = ref<SaveEventDto>({
    name: '',
    timestamp: '',
    place: '',
    info: ''
  });

  const $v = useVuelidate(rules, eventForm);

  async function submitForm() {
    await $v.value.$validate().then(async(result) => {
      if (result) {
        await eventsApi.saveEvent(eventForm.value);
        await toHomePage();
      }
    });
  }

  async function toHomePage() {
    await router.push('/');
  }
</script>
