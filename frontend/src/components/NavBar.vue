<template>
  <nav class="navbar mb-3 bg-white navbar-expand-sm">
    <div class="container-fluid">
      <span class="navbar-brand mx-sm-4">
        <img
          src="@/assets/images/logo.svg"
          alt="logo"
        >
      </span>
      <button
        ref="navbarToggler"
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNavAltMarkup"
        aria-controls="navbarNavAltMarkup"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"/>
      </button>
      <div
        id="navbarNavAltMarkup"
        class="collapse navbar-collapse"
      >
        <div class="navbar-nav">
          <router-link
            class="nav-link d-flex align-items-center px-3"
            active-class="active"
            to="/"
            exact
            @click="hideNavBar"
          >
            Avaleht
          </router-link>
          <router-link
            class="nav-link d-flex align-items-center px-3"
            active-class="active"
            to="/add-event"
            @click="hideNavBar"
          >
            Ürituse lisamine
          </router-link>
        </div>
      </div>
      <img
        src="@/assets/images/symbol.svg"
        alt="symbol"
        class="mx-4 d-none d-md-block"
      >
    </div>
  </nav>
</template>

<script setup lang="ts">
  import { ref } from 'vue';

  const navbarToggler = ref<HTMLButtonElement>();

  function hideNavBar() {
    if (!navbarToggler.value?.classList.contains('collapsed')) {
      navbarToggler.value?.click();
    }
  }
</script>

<style lang="scss" scoped>
  @import "@/assets/scss/variables";

  @media (min-width: $screen-sm) {
    .navbar {
      height: 80px;
      font-weight: bold;
      padding: 0;
    }
    .navbar > .container-fluid, .navbar-collapse, .nav-link, .navbar-nav {
      height: 100%;
    }
    .navbar-nav {
      margin-left: 10%;
    }
  }

  nav .nav-link.router-link-active,
  nav .nav-link.router-link-exact-active {
    background-color: $secondary-color;
    color: white;
  }

  .nav-link:hover {
    cursor: pointer;
    color: inherit;
    background-color: color-mix(in srgb, $secondary-color, transparent 70%);
  }

  .navbar-toggler:focus {
    box-shadow: none;
  }
</style>
