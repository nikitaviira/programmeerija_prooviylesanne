<template>
  <div id="personForm">
    <div class="row mb-3">
      <label class="col-sm-3 col-form-label">
        Eesnimi:
      </label>
      <div class="col-sm-9">
        <input
          id="personFirstName"
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
          id="personLastName"
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
          id="personPersonalCode"
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
          Isikukood ei ole valiidne
        </div>
      </div>
    </div>

    <div class="row mb-3">
      <label class="col-sm-3 col-form-label">
        Maksmise viis:
      </label>
      <div class="col-sm-9">
        <select
          id="personPaymentType"
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
          id="personInfo"
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
  </div>
</template>

<script setup lang="ts">
  import { required } from '@vuelidate/validators';
  import { ref, watch } from 'vue';
  import { PaymentType, type PersonDto } from '@/api/types';
  import useVuelidate from '@vuelidate/core';
  import { personalCodeIsValid } from '@/util/validation';
  import WordCounter from '@/components/WordCounter.vue';

  defineExpose({ clearForm });

  const emit = defineEmits<{
    (e: 'saved', value: PersonDto): void,
    (e: 'back'): void
  }>();

  const props = defineProps<{ person?: PersonDto }>();

  const rules = {
    firstName: { required },
    lastName: { required },
    personalCode: { required, personalCodeIsValid },
    paymentType: { required }
  };

  const personForm = ref<PersonDto>({
    firstName: '',
    lastName: '',
    personalCode: '',
    paymentType: undefined,
    info: ''
  });

  const $v = useVuelidate(rules, personForm);

  watch(() => props.person, (value) => {
    if (value) {
      personForm.value = Object.assign({}, props.person);
    }
  });

  async function submitForm() {
    await $v.value.$validate().then(async(result) => {
      if (result) {
        emit('saved', personForm.value);
      }
    });
  }

  function clearForm() {
    personForm.value = {
      firstName: '',
      lastName: '',
      personalCode: '',
      paymentType: undefined,
      info: ''
    };

    $v.value.$reset();
  }
</script>
