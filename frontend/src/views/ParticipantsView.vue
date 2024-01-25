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
          {{ eventDetails.name }}
        </div>
      </div>

      <div class="row mb-3">
        <div class="col-sm-3">
          Toimumisaeg:
        </div>
        <div class="col-sm-9">
          {{ eventDetails.datetime }}
        </div>
      </div>

      <div class="row mb-3">
        <div class="col-sm-3">
          Koht:
        </div>
        <div class="col-sm-9">
          {{ eventDetails.place }}
        </div>
      </div>

      <p>Osavõtjad:</p>
      <div
        v-if="eventDetails.participants.length > 0"
        class="grid"
      >
        <template
          v-for="(participant, index) in eventDetails.participants"
          :key="index"
        >
          <div class="g-col-1">
            {{ index + 1 }}.
          </div>
          <div class="g-col-5">
            {{ participant.fullName }}
          </div>
          <div class="g-col-4">
            {{ participant.code }}
          </div>
          <div class="g-col-2 text-center">
            <img
              class="action-icon mx-2"
              src="@/assets/images/eye.svg"
              @click="toParticipantDetails(participant)"
            >
            <img
              class="action-icon"
              src="@/assets/images/remove.svg"
              @click="removeParticipantFromEvent(participant)"
            >
          </div>
        </template>
      </div>
      <p v-else>
        Ei ole ühtegi osavõtja
      </p>

      <p class="form-title fw-normal fs-3 mt-5">
        Osavõtjate lisamine
      </p>

      <div class="row mb-3">
        <div class="col-sm-3" />
        <div class="col-sm-9">
          <div class="d-flex gap-4">
            <div class="form-check">
              <input
                v-model="participantTypeSelector"
                :value="ParticipantType.PERSON"
                class="form-check-input"
                type="radio"
              >
              <label class="form-check-label">Eraisik</label>
            </div>
            <div class="form-check">
              <input
                v-model="participantTypeSelector"
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
        v-if="participantTypeSelector === ParticipantType.COMPANY"
        ref="companyForm"
        @saved="saveCompany"
        @back="toHomePage"
      />

      <PersonForm
        v-if="participantTypeSelector === ParticipantType.PERSON"
        ref="personForm"
        @saved="savePerson"
        @back="toHomePage"
      />
    </div>
  </Form>
</template>

<script setup lang="ts">
  import Form from '@/components/Form.vue';
  import { onBeforeMount, ref } from 'vue';
  import CompanyForm from '@/components/CompanyForm.vue';
  import PersonForm from '@/components/PersonForm.vue';
  import { useRouter } from 'vue-router';
  import EventApi from '@/api/controllers/event';
  import CompanyApi from '@/api/controllers/company';
  import PersonApi from '@/api/controllers/person';
  import {
    type CompanyDto,
    type EventDetailsDto,
    type EventParticipantDto,
    ParticipantType,
    type PersonDto
  } from '@/api/types';

  const router = useRouter();
  const participantTypeSelector = ref<ParticipantType>(ParticipantType.PERSON);
  const eventId: number = parseInt(router.currentRoute.value.params.id as string);
  const companyForm = ref(null);
  const personForm = ref(null);
  const eventDetails = ref<EventDetailsDto>({
    id: undefined,
    datetime: '',
    name: '',
    place: '',
    participants: []
  });

  onBeforeMount(() => {
    loadEventDetails();
  });

  // Lisasin ts-ignore, sest meetodid, mis on lisatud defineExpose kaudu, ei ole IDEA poolt ära tuntud.
  async function saveCompany(company: CompanyDto) {
    await CompanyApi.saveCompany(eventId, company);
    // @ts-ignore
    companyForm.value.clearForm();
    await loadEventDetails();
  }

  // Lisasin ts-ignore, sest meetodid, mis on lisatud defineExpose kaudu, ei ole IDEA poolt ära tuntud.
  async function savePerson(person: PersonDto) {
    await PersonApi.savePerson(eventId, person);
    // @ts-ignore
    personForm.value.clearForm();
    await loadEventDetails();
  }

  async function loadEventDetails() {
    const { data } = await EventApi.eventDetails(eventId);
    eventDetails.value = data;
  }

  async function toHomePage() {
    await router.push('/');
  }

  async function toParticipantDetails(participant: EventParticipantDto) {
    if (participant.participantType === ParticipantType.COMPANY) {
      await router.push(`/company/${participant.id}`);
    } else {
      await router.push(`/person/${participant.id}`);
    }
  }

  async function removeParticipantFromEvent(participant: EventParticipantDto) {
    if (participant.participantType === ParticipantType.COMPANY) {
      await EventApi.removeCompanyFromEvent(eventId, participant.id);
    } else {
      await EventApi.removePersonFromEvent(eventId, participant.id);
    }
    await loadEventDetails();
  }
</script>
