<template>
  <div class="create-event">
    <v-container fluid>
      <v-row>
        <!-- <<<< LEFT SIDE >>>> -->
        <v-col cols="6">
          <h2>Select a movie to watch</h2>
          <v-toolbar dark color="teal">
            <v-toolbar-title>Invite friends</v-toolbar-title>
            <v-autocomplete
              v-model="select"
              :loading="loading"
              :items="items"
              :search-input.sync="search"
              cache-items
              class="mx-4"
              flat
              hide-no-data
              hide-details
              label="Type a name to search .."
              solo-inverted
            ></v-autocomplete>
          </v-toolbar>
        </v-col>
        <!-- >>>> RIGHT SIDE <<<< -->
        <v-col cols="6">
          <vue-cal
            class="vuecal--blue-theme"
            default-view="week"
            :disable-views="['years', 'year', 'month']"
            style="height: 600px"
            :events="events"
          ></vue-cal>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import VueCal from "vue-cal";
import "vue-cal/dist/vuecal.css";

export default {
  name: "createEvent",

  components: {
    VueCal
  },

  data: () => ({
    events: [
      {
        title: "Testest",
        start: "2019-12-11 18:00",
        end: "2019-12-11 20:00"
      }
    ],
    loading: false,
    items: [],
    search: null,
    select: null
  }),

  methods: {
    getSearchedUsers(val){
      this.loading = true;
    }
  },

  watch: {
    search(val) {
      val && val !== this.select && this.getSearchedUsers(val);
    }
  },

};
</script>

<style scoped>
</style>