<template>
  <div class="home">
    <h1>Home</h1>
    <p>
      This is a site where you can book movie nights with your friends. Search and read about films. 
    </p>

    <div>
      <p>
        If you believe that your account has been accessed by a superhacker, you can use the button below to invalidate all the logins and force them to reauthenticate with the tokenprovier. 
      </p>
      <v-btn class="error" @click="destroyAllLoggedIn">LOGOUT EVERYWHERE</v-btn>
    </div>
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
    async destroyAllLoggedIn(){
      await fetch(window.location.origin + '/api/auth', {
        method: 'DELETE', 
        headers: {
          "Authorization": "Bearer " + this.$store.state.token
        }
      })
      this.$router.push({path: "/login"})
    }
  }
};
</script>

<style scoped>

</style>
