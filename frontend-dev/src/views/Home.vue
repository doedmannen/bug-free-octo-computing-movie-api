<template>
  <div class="home">
    <h1>This is home</h1>
    <v-btn @click="test">Refresh accesstoken</v-btn>
  </div>
</template>

<script>

export default {
  name: 'login',

  components: {
      //
  },

  data: () => ({
    //
  }),

  methods: {
      async test(){
        let result = await fetch("api/users/refresh-access-token", {
          method: "PUT",
          headers: { "Content-type" : "application/json", "authorization" : "Bearer " + this.$store.state.token },
        })
      }
  }
};
</script>

<style scoped>

</style>
