<template>
  <div class="row mb-3">
    <label class="col-sm-3 col-form-label">
      Juriidiline nimi:
    </label>
    <div class="col-sm-9">
      <input
        v-model.trim="companyForm.name"
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
      Registrikood:
    </label>
    <div class="col-sm-9">
      <input
        v-model.trim="companyForm.registryCode"
        type="text"
        :class="{'is-invalid': $v.registryCode.$error}"
        class="form-control"
        @input="$v.registryCode.$touch()"
      >
      <div
        v-if="$v.registryCode.$dirty && $v.registryCode.required.$invalid"
        class="invalid-feedback d-block"
      >
        See väli on kohustuslik
      </div>
      <div
        v-else-if="$v.registryCode.$dirty && $v.registryCode.registryCodeIsValid.$invalid"
        class="invalid-feedback d-block"
      >
        Registrikood ei ole valiidne
      </div>
    </div>
  </div>

  <div class="row mb-3">
    <label class="col-sm-3 col-form-label">
      Osavõtjate arv:
    </label>
    <div class="col-sm-9">
      <input
        v-model.trim="companyForm.participantsCount"
        type="number"
        :class="{'is-invalid': $v.participantsCount.$error}"
        class="form-control"
        @input="$v.participantsCount.$touch()"
      >
      <div
        v-if="$v.participantsCount.$dirty && $v.participantsCount.required.$invalid"
        class="invalid-feedback d-block"
      >
        See väli on kohustuslik
      </div>
      <div
        v-else-if="$v.participantsCount.$dirty && $v.participantsCount.maxValue.$invalid"
        class="invalid-feedback d-block"
      >
        Maksimaalne osavõtjate arv on 100
      </div>
    </div>
  </div>

  <div class="row mb-3">
    <label class="col-sm-3 col-form-label">
      Maksmise viis:
    </label>
    <div class="col-sm-9">
      <select
        v-model="companyForm.paymentType"
        :class="{'is-invalid': $v.paymentType.$error}"
        class="form-select"
      >
        <option
          selected
          hidden
          :value="undefined"
        >
          Valige maksmise viis
        </option>
        <option
          :value="PaymentType.BANK_TRANSFER"
        >
          Pangaülekanne
        </option>
        <option :value="PaymentType.CASH">
          Sularaha
        </option>
      </select>
      <div
        v-if="$v.paymentType.$dirty && $v.paymentType.$error"
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
        v-model.trim="companyForm.info"
        maxlength="5000"
        class="form-control"
      />
      <WordCounter
        :length="companyForm.info.length"
        :max-length="5000"
      />
    </div>
  </div>

  <button
    type="button"
    class="btn back-btn me-2"
    @click="emit('back')"
  >
    Tagasi
  </button>

  <button
    type="button"
    class="btn submit-btn"
    @click.prevent="submitForm"
  >
    Salvesta
  </button>
</template>

<script setup lang="ts">
  import { maxValue, required } from '@vuelidate/validators';
  import { ref, watch } from 'vue';
  import { type CompanyDto, PaymentType } from '@/api/types';
  import useVuelidate from '@vuelidate/core';
  import { registryCodeIsValid } from '@/util/validation';
  import WordCounter from '@/components/WordCounter.vue';

  defineExpose({ clearForm });

  const emit = defineEmits<{
    (e: 'saved', value: CompanyDto): void,
    (e: 'back'): void
  }>();

  const props = defineProps<{ company?: CompanyDto }>();

  const rules = {
    name: { required },
    registryCode: { required, registryCodeIsValid },
    participantsCount: { required, maxValue: maxValue(100) },
    paymentType: { required }
  };

  const companyForm = ref<CompanyDto>({
    name: '',
    registryCode: '',
    participantsCount: undefined,
    paymentType: undefined,
    info: ''
  });

  const $v = useVuelidate(rules, companyForm);

  watch(() => props.company, (value) => {
    if (value) {
      companyForm.value = Object.assign({}, props.company);
    }
  });

  async function submitForm() {
    await $v.value.$validate().then(async(result) => {
      if (result) {
        emit('saved', companyForm.value);
      }
    });
  }

  function clearForm() {
    companyForm.value = {
      name: '',
      registryCode: '',
      participantsCount: undefined,
      paymentType: undefined,
      info: ''
    };

    $v.value.$reset();
  }
</script>
