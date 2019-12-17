<template>
  <v-app>
    <TopNavBar />

    <v-content class="pa-5">
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>

import TopNavBar from '@/components/TopNavBar'

export default {
  name: "App",

  components: {
    TopNavBar
  },

  data: () => ({
    //
  }),
  mounted() {
    // Fixes redirects from backend pahts
    if (window.location.search.startsWith("?redirect=")) {
      this.$router.push({ path: window.location.search.replace("?redirect=","") });
    }
    // Check token validation
    this.$store.dispatch("tokenLookup")
    setInterval(() => {
      this.$store.dispatch("tokenLookup")
    }, 60000);
  }
};
</script>
