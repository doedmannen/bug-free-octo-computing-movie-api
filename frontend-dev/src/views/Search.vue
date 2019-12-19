<template>
  <div>
    <v-container fluid>
      <v-row justify="center">
        <div class="searchbar">
          <v-row align="center">
            <v-text-field
              @keyup.enter="search"
              label="Search"
              v-model="input"
              append-icon="mdi-magnify"
              @click:append="search"
              :rules="mincharacters"
            ></v-text-field>
            <v-btn @click="search" class="search-button mb-2 ml-3">Search</v-btn>
          </v-row>
          <div v-if="loaded && exist">
            <v-btn @click="previouspage" :disabled="page === 1">previous</v-btn>
            {{page}}/
            {{max}}
            <v-btn @click="nextpage" :disabled="page === this.max">Next</v-btn>
          </div>
        </div>
      </v-row>
      <v-row>
        <div class="search-results">
          <v-col cols="12">
            <v-row justify="space-between" v-if="searchResult.result">
              <v-card
                class="mt-4 movie-card"
                max-width="275px"
                shaped
                hover
                v-for="(value) in searchResult.result.Search"
                :key="value.imdbID"
                :to="{ path : `/movie/`+ value.Title.split('/').join(' ')}"
              >
                <v-img
                  min-width="275px"
                  max-width="275px"
                  min-height="350px"
                  max-height="350px"
                  class="movie-card-image"
                  :src="value.Poster === 'N/A' ? `https://imgplaceholder.com/275x350/cccccc/757575/glyphicon-film?text=No+image+found&font-family=CaviarDreams` : value.Poster"
                ></v-img>
                <v-card-title class="pa-2">
                  <p class="movie-title">{{value.Title}}</p>
                </v-card-title>
              </v-card>
            </v-row>
            <div v-if="!exist && loaded">
              <h1>Can`t find film</h1>
            </div>
          </v-col>
        </div>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  name: "search",

  components: {
    //
  },

  data: () => ({
    searchResult: {},
    loaded: false,
    exist: false,
    input: "",
    page: 1,
    max: 0,
    mincharacters: [v => v.length >= 3 || "Min 3 tecken"]
  }),
  methods: {
    nextpage() {
      this.page++;
      this.getSearch();
    },
    previouspage() {
      this.page--;
      this.getSearch();
    },
    getmaxpage() {
      this.max = this.searchResult.result.totalResults;
      this.max = this.max / 10;
      this.max = Math.ceil(this.max);
    },
    goTo(title) {
      this.$router.push({ path: "/movie/" + title.split("/").join(" ") });
    },
    search() {
      this.getSearch();
    },
    async getSearch() {
      let url =
        window.location.origin +
        "/api/movie/search/?p=" +
        this.page +
        "&s=" +
        this.input;
      let response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization: "Bearer " + this.$store.state.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        this.searchResult = await response.json();
        this.exist = true;
        this.getmaxpage();
      } else {
        this.searchResult = {};
        this.exist = false;
      }
      this.loaded = true;
    }
  }
};
</script>
<style scoped>
.search-button {
  margin-bottom: 5px;
}

.movie-title {
  font-size: 1rem;
  margin: 0;
}

.movie-card-image {
  opacity: 0.8;
}

.movie-card-image:hover {
  opacity: 1;
}
</style>>
