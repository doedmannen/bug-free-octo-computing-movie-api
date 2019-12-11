<template>
  <div class="create-event">
    <v-container fluid>
      <v-row>
        <v-col cols="6">
          <h1>This is create a new event page</h1>
        </v-col>
        <v-col cols="6">
          <v-sheet height="600">
            <v-calendar type="day" now="today" value="today" :events="events" :interval-format="changeIntervalFormat"></v-calendar>
          </v-sheet>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  name: "createEvent",

  components: {
    //
  },

  methods: {

    changeIntervalFormat(date){
      return date.time;
    }
  },

  data: () => ({
    events: [
      {
        name: 'Testest',
        start: '2019-12-11 18:00',
        end: '2019-12-11 20:00',
      }
    ]
  })
};
</script>

<style scoped>
</style>