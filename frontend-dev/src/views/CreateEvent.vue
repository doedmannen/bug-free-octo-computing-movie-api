<template>
  <div class="create-event">
    <v-container fluid>
      <v-row>
        <!-- <<<< LEFT SIDE >>>> -->
        <v-col cols="6">
          <v-card color="blue-grey darken-1" dark>
            <template v-slot:progress>
              <v-progress-linear absolute color="green lighten-3" height="4" indeterminate></v-progress-linear>
            </template>
            <v-img
              height="200"
              src="http://images.summitmedia-digital.com/smartpar/images/2017/12/30/movienight-web.jpg"
            >
              <v-row>
                <v-row class="pa-4" align="center" justify="center">
                  <v-col cols="6" class="text-center">
                    <h3 class="headline">Create a new movie night</h3>
                  </v-col>
                </v-row>
              </v-row>
            </v-img>
            <v-form>
              <v-container>
                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="nameOfEvent"
                      filled
                      color="blue-grey lighten-2"
                      label="Name"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-autocomplete
                      v-model="peopleToInvite"
                      :loading="loading"
                      :items="items"
                      :search-input.sync="search"
                      chips
                      filled
                      multiple
                      label="Type a name to search .."
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
              <v-btn color="blue-grey darken-3" @click="checkAvailableTimes" depressed>
                <v-icon left>mdi-update</v-icon>Check available times
              </v-btn>
            </v-card-actions>
          </v-card>
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
    peopleToInvite: [],
    nameOfEvent: ""
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

      let result = await response.json();

      console.log(result);
      if (result.length > 0) {
        result.map(user => this.items.push(user.username));
      }
    },

    remove(item) {
      console.log(item);
      console.log(this.peopleToInvite);
      const index = this.peopleToInvite.indexOf(item);
      if (index >= 0) this.peopleToInvite.splice(index, 1);
    },

    checkAvailableTimes() {
      console.log(this.peopleToInvite);
      console.log(this.nameOfEvent);
    }

  },

  watch: {
    getSearch(val) {
      if (val !== null) {
        this.search = val.trim();
        setTimeout(() => {
          val &&
            val.trim().length >= 3 &&
            val !== this.peopleToInvite &&
            this.getSearchedUsers(val);
        }, 500);
      }
    }
  }
};
</script>

<style scoped>
.headline {
  background-color: rgba(0, 0, 0, 0.575);
  padding: 10px;
  color: whitesmoke;
  border-radius: 20px;
}
</style>