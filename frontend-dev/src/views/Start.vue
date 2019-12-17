<template>
  <div class="start">
    <v-row justify="center">
        <v-col cols="4"> 
          <p> This is a site where you can book movie nights with your friends.
                 Search and read about films. You can also read about jwt</p>
        </v-col>
      </v-row>
       <v-row justify="center">
        <v-col cols="4">
            <v-btn to="/login">Login</v-btn>
        </v-col>
       </v-row>           
  </div>
</template>
<script>
  export default {
    name: 'start',
  }
</script>
<style scoped>

</style>