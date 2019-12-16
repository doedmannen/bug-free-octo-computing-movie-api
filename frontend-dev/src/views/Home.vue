<template>
  <div class="home">
    <h1>Home</h1><p>
      This is a site where you can book movie nights with your friends.
      Search and read about films. 
      </p>
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
      async getCalendarInfo(){
        let result = await fetch("https://www.googleapis.com/calendar/v3/freeBusy",  {
          
        })
      }
  }
};
</script>

<style scoped>

</style>
