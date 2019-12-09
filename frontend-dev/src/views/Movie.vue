<template>
<div>
<template v-if="loaded">

<v-container fluid>
    <v-row>
      <div class="movie">
        <h1>{{movie.Title}}</h1>
        <v-row>
          <v-col cols="5">
            <v-img :src="movie.Poster"></v-img>
          </v-col>
          <v-col cols="5">
            <p>Längd: {{movie.Runtime}}</p>
            <p>Handlar om: {{movie.Plot}}</p>
            <p>Ganger: {{movie.Genre}}</p>
            <p>Åldersgräns {{movie.Rated}}</p>
            <p>Utgivnings år: {{movie.Year}}</p>
            <p>Regissörer: {{movie.Director}}</p>
            <p>Skådespelare: {{movie.Actors}}</p>
            <p>
              <b>Ratings</b>
            </p>
            <div v-for="(value, index) in movie.Ratings" :key="index">
              <p>{{value.Source}}: {{value.Value}}</p>
            </div>
            <p>Metascore: {{movie.Metascore}}/100</p>
            <p>ImdbRating: {{movie.imdbRating}}/10</p>
            <v-btn v-on:click="bok">Boka film</v-btn>
          </v-col>
        </v-row>
      </div>
    </v-row>
  </v-container>
</template>
  <template v-else>
  <h1>Hittar inte filmen</h1>
</template>
</div>
</template>

<script>
export default {
  name: "movie",
  data: () => ({
    movie: [],
    loaded: false
  }),
  mounted() {
    this.getMovie();
  },
  methods: {
    bok() {
      //TODO
    },
    async getMovie() {
      let url = window.location.origin + "/api/movie/?t=iron man";
      let response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization:
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE1NzU4ODE4ODIsImV4cCI6MTgzODg1NjYyNX0.SUbAdLlgsQMAn54EsgyLBBJfd8Nn07YO7eWc0DtenJs",
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        this.movie = await response.json();
        this.loaded = true;
      } else {
        this.loaded = false;
      }
    }
  }
};
</script>

<style>
/*
async
let url = window.location.origin + "/api/YOUR_PATH_FOR_FETCH";
let response = await fetch(url, { 
   method: 'GET', 
   headers: {
     'Authorization': 'Bearer '+localStorage.getItem("token"), 
     'Content-Type': 'application/json'
   }, 
   body: JSON.stringify({  })
 });
if(response.status == 200)
    return await response.json();
else 
    return "något är fel";


 */
</style>