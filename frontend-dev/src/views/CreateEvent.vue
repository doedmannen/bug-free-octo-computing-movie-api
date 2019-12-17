<template>
  <div class="create-event">
    <v-container fluid>
      <v-row>
        <!-- <<<< LEFT SIDE >>>> -->
        <v-col cols="5">
          <v-card color="blue-grey darken-3" dark>
            <v-img
              height="250"
              src="http://images.summitmedia-digital.com/smartpar/images/2017/12/30/movienight-web.jpg"
            >
              <v-row>
                <v-row class="pa-4" align="center" justify="center">
                  <v-col cols="8" class="text-center">
                    <h3 class="headline">Create a new movie night</h3>
                  </v-col>
                </v-row>
              </v-row>
            </v-img>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" md="12">
                    <v-autocomplete
                      v-model="selectedMovie"
                      :loading="loadingMovie"
                      :items="items2"
                      item-text="Title"
                      :search-input.sync="searchMovie"
                      chips
                      filled
                      hide-no-data
                      label="Search for a movie"
                    >
                      <template v-slot:selection="data">
                        <v-chip
                          v-bind="data.attrs"
                          :input-value="data.selected"
                          close
                          @click="data.select"
                        >
                          <v-avatar left>
                            <v-icon>mdi-filmstrip</v-icon>
                          </v-avatar>
                          {{ data.item.Title }}
                        </v-chip>
                      </template>
                    </v-autocomplete>
                  </v-col>
                  <v-col cols="12">
                    <v-autocomplete
                      v-model="peopleToInvite"
                      :loading="loadingUser"
                      :items="items"
                      :search-input.sync="searchUsers"
                      chips
                      filled
                      hide-no-data
                      multiple
                      label="Search for people to invite"
                    >
                      <template v-slot:selection="data">
                        <v-chip
                          v-bind="data.attrs"
                          :input-value="data.selected"
                          close
                          @click="data.select"
                          @click:close="remove(data.item)"
                        >
                          <v-avatar left>
                            <v-icon>mdi-account</v-icon>
                          </v-avatar>
                          {{ data.item }}
                        </v-chip>
                      </template>
                    </v-autocomplete>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
            <v-divider></v-divider>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue-grey darken-3"
                :disabled="!validateEmpty"
                :loading="loadingAvailableTimes"
                @click="checkAvailableTimes"
              >
                <v-icon left>mdi-update</v-icon>Check available times
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
        <!-- >>>> RIGHT SIDE <<<< -->
        <v-col cols="7">
          <vue-cal
            class="vuecal--blue-theme"
            default-view="week"
            :disable-views="['years', 'year', 'month']"
            style="height: 630px"
            small
            :events="events"
            :on-event-click="onEventClick"
          ></vue-cal>
          <v-dialog v-model="showDialog" width="600px">
            <v-card>
              <v-card-title>
                <v-icon>{{ selectedEvent.icon }}</v-icon>
                <span>Book a movie</span>
                <v-spacer />
                <strong>{{ selectedEvent.start && selectedEvent.start }}</strong>
              </v-card-title>
              <v-card-text>
                <p v-html="selectedEvent.contentFull" />
                <strong>Event details:</strong>
                <ul>
                  <li>Movie: {{ this.selectedMovie }}</li>
                  <li>Event starts at: {{ selectedEvent.startDate && selectedEvent.startDate }}</li>
                  <li>Event ends at: {{ selectedEvent.endDate && selectedEvent.endDate }}</li>
                </ul>
                <v-btn @click="createEvent(selectedEvent.start)">Create event</v-btn>
              </v-card-text>
            </v-card>
          </v-dialog>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import VueCal from "vue-cal";
import "vue-cal/dist/vuecal.css";
import { setTimeout } from "timers";
import Moment from "moment";

export default {
  name: "createEvent",

  components: {
    VueCal
  },

  data: () => ({
    selectedEvent: {},
    showDialog: false,
    events: [],
    loadingUser: false,
    loadingMovie: false,
    loadingAvailableTimes: false,
    items: [],
    items2: [],
    usersToInviteValid: false,
    selectedMovieValid: false,
    searchUsers: null,
    searchMovie: null,
    peopleToInvite: [],
    selectedMovie: ""
  }),

  mounted() {
    if (this.$route.params.title) {
      this.selectedMovie = this.$route.params.title;
      this.items2 = [{ Title: this.selectedMovie }];
    }
  },
  computed: {
    getSearch() {
      return this.searchUsers;
    },

    getMovieSearch() {
      return this.searchMovie;
    },

    validateEmpty() {
      return this.peopleToInvite.length !== 0 && this.selectedMovie !== "";
    }
  },

  methods: {
    async getSearchedUsers() {
      this.loadingUser = true;
      let response = await fetch(
        "api/users/search?username=" + this.searchUsers,
        {
          method: "GET",
          headers: {
            Authorization: "Bearer " + this.$store.state.token,
            "Content-Type": "application/json"
          }
        }
      );

      let result = await response.json();

      if (result.length > 0) {
        result.map(user => this.items.push(user.username));
        this.loadingUser = false;
      }
    },

    async getSearchedMovies() {
      this.loadingMovie = true;
      let response = await fetch(
        "api/movie/search/?p=1&s=" + this.searchMovie,
        {
          method: "GET",
          headers: {
            Authorization: "Bearer " + this.$store.state.token,
            "Content-Type": "application/json"
          }
        }
      );

      if (response.status === 200) {
        let result = await response.json();

        if (result.result.Search.length > 0) {
          result.result.Search.map(movie => this.items2.push(movie));
        }
      }
      this.loadingMovie = false;
    },

    remove(item) {
      const index = this.peopleToInvite.indexOf(item);
      if (index >= 0) this.peopleToInvite.splice(index, 1);
    },

    async checkAvailableTimes() {
      this.loadingAvailableTimes = true;
      const url = `api/calendar?users=${this.peopleToInvite}&movieTitle=${this.selectedMovie}`;

      let response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization: "Bearer " + this.$store.state.token,
          "Content-Type": "application/json"
        }
      });

      let result = await response.json();

      let tempArray = [];

      if (response.status === 200) {
        result.map(item =>
          tempArray.push({
            start: Moment(item.start).format("YYYY-MM-DD HH:mm"),
            end: Moment(item.stop).format("YYYY-MM-DD HH:mm")
          })
        );
        this.events = tempArray;
      }
      this.loadingAvailableTimes = false;
    },

    async createEvent(startTime) {
      let ms = Date.parse(startTime);
      const url = `api/calendar?users=${this.peopleToInvite}&movieTitle=${this.selectedMovie}&start=${ms}`;

      let response = await fetch(url, {
        method: "POST",
        headers: {
          Authorization: "Bearer " + this.$store.state.token,
          "Content-Type": "application/json"
        }
      });

      if (response.status === 200) {
        this.events = [];
        this.selectedMovie = "";
        this.peopleToInvite = [];
        this.showDialog = false;
      }
    },

    onEventClick(event, e) {
      this.selectedEvent = event;
      this.showDialog = true;
      e.stopPropagation();
    }
  },

  watch: {
    getSearch(val) {
      if (val !== null) {
        this.searchUsers = val.trim();
        setTimeout(() => {
          val &&
            val.trim().length >= 3 &&
            val !== this.peopleToInvite &&
            this.getSearchedUsers();
        }, 1000);
      }
    },

    getMovieSearch(val) {
      if (val !== null) {
        this.searchMovie = val.trim();
        setTimeout(() => {
          val &&
            val.trim().length >= 3 &&
            val !== this.selectedMovie &&
            this.getSearchedMovies();
        }, 1000);
      }
    }
  }
};
</script>

<style>
.headline {
  background-color: rgba(0, 0, 0, 0.664);
  padding: 10px;
  color: whitesmoke;
  border-radius: 20px;
}

.vuecal__event {
  cursor: pointer;
  background-color: rgba(103, 180, 198, 0.664);
}
</style>