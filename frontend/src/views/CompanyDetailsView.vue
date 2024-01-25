<template>
  <Form title="Osavõtja info">
    <div class="form-container p-4">
      <p class="form-title fw-normal fs-3">
        Osavõtja info
      </p>
      <CompanyForm
        :company="company"
        @saved="updateCompany"
        @back="toHomePage"
      />
    </div>
  </Form>
</template>

<script setup lang="ts">
  import CompanyForm from '@/components/CompanyForm.vue';
  import Form from '@/components/Form.vue';
  import { useRouter } from 'vue-router';
  import { onBeforeMount, ref } from 'vue';
  import CompanyApi from '@/api/controllers/company';
  import type { CompanyDto } from '@/api/types';

  const router = useRouter();
  const companyId: number = parseInt(router.currentRoute.value.params.id as string);
  const company = ref<CompanyDto>();

  onBeforeMount(async() => {
    const { data } = await CompanyApi.company(companyId);
    company.value = data;
  });

  async function updateCompany(company: CompanyDto) {
    await CompanyApi.updateCompany(companyId, company);
    await toHomePage();
  }

  async function toHomePage() {
    await router.push('/');
  }
</script>
