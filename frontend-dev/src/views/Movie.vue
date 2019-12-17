<template>
<div>
<template v-if="loaded && exist">
<v-container fluid>
    <v-row>
      <div class="movie">
        <h1>{{movie.Title}}</h1>
        <v-row>
          <v-col cols="5">
            <v-img :src="movie.Poster"></v-img>
          </v-col>
          <v-col cols="5">
            <p>Time: {{movie.Runtime}}</p>
            <p>About: {{movie.Plot}}</p>
            <p>Genres: {{movie.Genre}}</p>
            <p>Rated: {{movie.Rated}}</p>
            <p>Release: {{movie.Year}}</p>
            <p>Director: {{movie.Director}}</p>
            <p>Stars: {{movie.Actors}}</p>
            <p>
              <b>Ratings</b>
            </p>
            <div v-for="(value, index) in movie.Ratings" :key="index">
              <p>{{value.Source}}: {{value.Value}}</p>
            </div>
            <p>Metascore: {{movie.Metascore}}/100</p>
            <p>ImdbRating: {{movie.imdbRating}}/10</p>
            <v-btn v-on:click="bok">Book film</v-btn>
          </v-col>
        </v-row>
      </div>
    </v-row>
  </v-container>
</template>
<template v-else-if="!loaded && !exist">
  <h1>loading</h1>
</template>
  <template v-else>
  <h1>Can`t find film</h1>
</template>
</div>
</template>

<script>
export default {
  name: "movie",
  data: () => ({
    movie: [],
    loaded: false,
    exist: false,
  }),
  mounted() {
    this.getMovie();
  },
  methods: {
    bok() {
      //TODO
    },
    async getMovie() { 
      let url = window.location.origin + "/api/movie/?t=" + this.$route.params.title;
      let response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization:
            "Bearer "+this.$store.state.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        this.movie = await response.json();
        this.exist = true;
      } else {
        this.exist = false;
      }
        this.loaded = true;
    }
  }
};
</script>
<style scoped>

</style>