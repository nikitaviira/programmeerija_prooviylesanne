<template>
  <footer class="text-light">
    <div class="mt-3">
      <div class="row p-5">
        <div class="footer-col col-lg-3 col-sm-6 mb-4 mb-lg-0">
          <h3 class="fw-normal">
            Curabitur
          </h3>
          <p>Emauris</p>
          <p>Kfringilla</p>
          <p>Oin magna sem</p>
          <p>Kelementum</p>
        </div>

        <div class="footer-col col-lg-3 col-sm-6 mb-4 mb-lg-0">
          <h3 class="fw-normal">
            Fusce
          </h3>
          <p>Econsectetur</p>
          <p>Ksollicitudin</p>
          <p>Omvulputate</p>
          <p>Nunc fringilla tellu</p>
        </div>

        <div class="footer-col col-lg-3 col-sm-6 mb-2 mb-lg-0">
          <h3 class="fw-normal">
            Kontakt
          </h3>
          <p><b>Peakontor: Tallinnas</b></p>
          <p>Väike- Ameerika 1, 11415 Tallinn</p>
          <p>Telefon: 605 4450</p>
          <p>Faks: 605 3186</p>
        </div>

        <div class="footer-col col-lg-3 col-sm-6">
          <h3 class="fw-normal empty-title d-none d-sm-block"/>
          <p><b>Harukontor: Võrus</b></p>
          <p>Oja tn 7 (külastusaadress)</p>
          <p>Telefon: 605 3330</p>
          <p>Faks: 605 3155</p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">

</script>

<style lang="scss" scoped>
  @import "@/assets/scss/variables";

  footer {
    background: $footer-color;
  }

  p {
    margin: 0;
  }

  .empty-title {
    &:before {
      content: "-";
    }
    visibility: hidden;
  }

  .footer-col p:first-of-type {
    margin-top: 15px;
  }
</style>
