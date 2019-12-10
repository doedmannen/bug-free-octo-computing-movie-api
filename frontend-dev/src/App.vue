<template>
  <v-app>
    <TopNavBar />

    <v-content>
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
    this.$store.dispatch("tokenLookup");
  }
};
</script>
