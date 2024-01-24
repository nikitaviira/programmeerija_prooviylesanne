<template>
  <div class="row w-100 g-0 form-header">
    <div class="col-md-3 col-sm-4 text-col d-flex align-items-center justify-content-center">
      <p class="text-white m-0">
        {{ title }}
      </p>
    </div>

    <div class="col-md-9 col-sm-8 h-100">
      <img
        src="@/assets/images/libled.jpg"
        class="img-fluid w-100 h-100 object-fit-cover"
      >
    </div>
  </div>
</template>

<script setup lang="ts">
  defineProps<{ title: string }>();
</script>

<style lang="scss" scoped>
@import "@/assets/scss/variables";

.form-header {
  padding: 0;
  height: 80px;
}

.text-col {
  height: inherit;
  background-color: $secondary-color;

  p {
    font-size: clamp(1.4rem, 1.7vw, 1.6rem);
  }
}

@media (max-width: $screen-sm) {
  .form-header {
    height: 80px;
    margin-top: 80px;
    flex-flow: column-reverse;
  }
}
</style>
