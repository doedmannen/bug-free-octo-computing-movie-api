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
              multiple
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
import { setTimeout } from "timers";

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

  computed: {
    getSearch() {
      return this.search;
    }
  },

  methods: {
    async getSearchedUsers(val) {
      this.loading = true;
      let response = await fetch("api/users/search?username=" + this.search, {
        method: "GET",
        headers: {
          Authorization: "Bearer " + this.$store.state.token,
          "Content-Type": "application/json"
        }
      });

      let result = await response.json()

      console.log(result);
      result.map(user => this.items.push(user.username))
    }
  },

  watch: {
    getSearch(val) {
      this.search = val.trim()
      setTimeout(() => {
        val && val.trim().length >= 3 && val !== this.select && this.getSearchedUsers(val);
      }, 500);
    }
  }
};
</script>

<style scoped>
</style>