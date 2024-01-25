<template>
  <Form title="Osavõtja info">
    <div class="form-container p-4">
      <p class="form-title fw-normal fs-3">
        Osavõtja info
      </p>
      <PersonForm
        :person="person"
        @saved="updatePerson"
        @back="toHomePage"
      />
    </div>
  </Form>
</template>

<script setup lang="ts">
  import Form from '@/components/Form.vue';
  import { useRouter } from 'vue-router';
  import { onBeforeMount, ref } from 'vue';
  import PersonApi from '@/api/controllers/person';
  import { type PersonDto } from '@/api/types';
  import PersonForm from '@/components/PersonForm.vue';

  const router = useRouter();
  const personId: number = parseInt(router.currentRoute.value.params.id as string);
  const person = ref<PersonDto>({
    firstName: '',
    lastName: '',
    personalCode: '',
    paymentType: undefined,
    info: ''
  });

  onBeforeMount(async() => {
    const { data } = await PersonApi.person(personId);
    person.value = data;
  });

  async function updatePerson(person: PersonDto) {
    await PersonApi.updatePerson(personId, person);
    await toHomePage();
  }

  async function toHomePage() {
    await router.push('/');
  }
</script>
