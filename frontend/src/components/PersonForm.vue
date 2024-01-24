<template>
  <div class="row mb-3">
    <label class="col-sm-3 col-form-label">
      Eesnimi:
    </label>
    <div class="col-sm-9">
      <input
        v-model.trim="personForm.firstName"
        type="text"
        :class="{'is-invalid': $v.firstName.$error}"
        class="form-control"
        @input="$v.firstName.$touch()"
      >
      <div
        v-if="$v.firstName.$dirty && $v.firstName.$error"
        class="invalid-feedback d-block"
      >
        See väli on kohustuslik
      </div>
    </div>
  </div>

  <div class="row mb-3">
    <label class="col-sm-3 col-form-label">
      Perenimi:
    </label>
    <div class="col-sm-9">
      <input
        v-model.trim="personForm.lastName"
        type="text"
        :class="{'is-invalid': $v.lastName.$error}"
        class="form-control"
        @input="$v.lastName.$touch()"
      >
      <div
        v-if="$v.lastName.$dirty && $v.lastName.$error"
        class="invalid-feedback d-block"
      >
        See väli on kohustuslik
      </div>
    </div>
  </div>

  <div class="row mb-3">
    <label class="col-sm-3 col-form-label">
      Isikukood:
    </label>
    <div class="col-sm-9">
      <input
        v-model.trim="personForm.personalCode"
        type="text"
        :class="{'is-invalid': $v.personalCode.$error}"
        class="form-control"
        @input="$v.personalCode.$touch()"
      >
      <div
        v-if="$v.personalCode.$dirty && $v.personalCode.required.$invalid"
        class="invalid-feedback d-block"
      >
        See väli on kohustuslik
      </div>
      <div
        v-else-if="$v.personalCode.$dirty && $v.personalCode.personalCodeIsValid.$invalid"
        class="invalid-feedback d-block"
      >
        Registrikood ei ole valiidne
      </div>
    </div>
  </div>

  <div class="row mb-3">
    <label class="col-sm-3 col-form-label">
      Maksmise viis:
    </label>
    <div class="col-sm-9">
      <select
        v-model="personForm.paymentType"
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
        v-model.trim="personForm.info"
        maxlength="1500"
        class="form-control"
      />
      <WordCounter
        :length="personForm.info.length"
        :max-length="1500"
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
  import { required } from '@vuelidate/validators';
  import { ref } from 'vue';
  import { PaymentType, type PersonDto } from '@/api/types';
  import useVuelidate from '@vuelidate/core';
  import { personalCodeIsValid } from '@/util/validation';
  import WordCounter from '@/components/WordCounter.vue';

  const emit = defineEmits<{
    (e: 'saved'): void,
    (e: 'back'): void
  }>();

  const rules = {
    firstName: { required },
    lastName: { required },
    personalCode: { required, personalCodeIsValid },
    paymentType: { required }
  };

  const personForm = ref<PersonDto>({
    id: null,
    firstName: '',
    lastName: '',
    personalCode: '',
    paymentType: undefined,
    info: ''
  });

  const $v = useVuelidate(rules, personForm);

  async function submitForm() {
    await $v.value.$validate().then(async(result) => {
      if (result) {
        emit('saved');
      }
    });
  }
</script>

<style scoped lang="scss">
@import "@/assets/scss/variables";

button {
  color: white;

  &.back-btn {
    background: grey;
  }

  &.submit-btn {
    background: $secondary-color;
  }
}
</style>