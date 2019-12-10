<template>
  <div class="home">
    <h1>This is home</h1>
    <v-btn @click="test">Refresh accesstoken</v-btn>
    <v-btn>Get calendar info</v-btn>
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
      },

      async getCalendarInfo(){
        let result = await fetch("https://www.googleapis.com/calendar/v3/freeBusy",  {
          
        })
      }
  }
};
</script>

<style scoped>

</style>
