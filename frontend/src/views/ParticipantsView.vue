<template>
  <Form title="Osavõtjad">
    <div class="form-container p-4">
      <p class="form-title fw-normal fs-3">
        Osavõtjad
      </p>

      <div class="row mb-3">
        <div class="col-sm-3">
          Ürituse nimi:
        </div>
        <div class="col-sm-9">
          Some event name
        </div>
      </div>

      <div class="row mb-3">
        <div class="col-sm-3">
          Toimumisaeg:
        </div>
        <div class="col-sm-9">
          18.02.2016 14:55
        </div>
      </div>

      <div class="row mb-3">
        <div class="col-sm-3">
          Koht:
        </div>
        <div class="col-sm-9">
          Some random place
        </div>
      </div>

      <p>Osavõtjad:</p>
      <div class="grid">
        <template
          v-for="n in 3"
          :key="n"
        >
          <div class="g-col-1">
            {{ n }}.
          </div>
          <div class="g-col-5">
            Liikluslab Baltic OU
          </div>
          <div class="g-col-4">
            39807093721
          </div>
          <div class="g-col-2 text-center">
            <img
              class="action-icon mx-2"
              src="@/assets/images/eye.svg"
            >
            <img
              class="action-icon"
              src="@/assets/images/remove.svg"
            >
          </div>
        </template>
      </div>

      <p class="form-title fw-normal fs-3 mt-5">
        Osavõtjate lisamine
      </p>

      <div class="row mb-3">
        <div class="col-sm-3" />
        <div class="col-sm-9">
          <div class="d-flex gap-4">
            <div class="form-check">
              <input
                v-model="participantType"
                :value="ParticipantType.PERSON"
                class="form-check-input"
                type="radio"
              >
              <label class="form-check-label">Eraisik</label>
            </div>
            <div class="form-check">
              <input
                v-model="participantType"
                :value="ParticipantType.COMPANY"
                class="form-check-input"
                type="radio"
              >
              <label class="form-check-label">Ettevõtte</label>
            </div>
          </div>
        </div>
      </div>

      <CompanyForm
        v-if="participantType === ParticipantType.COMPANY"
        @saved="() => {}"
        @back="toHomePage"
      />

      <PersonForm
        v-if="participantType === ParticipantType.PERSON"
        @saved="() => {}"
        @back="toHomePage"
      />
    </div>
  </Form>
</template>

<script setup lang="ts">
  import Form from '@/components/Form.vue';
  import { ref } from 'vue';
  import CompanyForm from '@/components/CompanyForm.vue';
  import PersonForm from '@/components/PersonForm.vue';
  import { useRouter } from 'vue-router';

  enum ParticipantType {
    COMPANY, PERSON
  }

  const router = useRouter();
  const participantType = ref<ParticipantType>(ParticipantType.PERSON);

  async function toHomePage() {
    await router.push('/');
  }
</script>
