<template>
<div>
<template ><!--v-if="loaded"-->

<v-container fluid>
    <v-row>
      <div class="search">
        <v-col cols="12" >
        <v-text-field label="Sök" v-model="input"></v-text-field>
        <v-btn @click="search">Sök</v-btn>
        <v-row v-if="searchResult.result">
             <div v-for="(value) in searchResult.result.Search" :key="value.imbdID">
              <p>{{value.Title}}</p>
              <v-col cols="8">
                <v-img :src="value.Poster === 'N/A' ? `https://image.shutterstock.com/image-vector/no-image-available-icon-template-260nw-1036735678.jpg` : value.Poster" @click="goTo(value.Title)"></v-img>
          </v-col>
          <p>{{value.Year}}</p>
            </div>
        </v-row>
        </v-col>
      </div>
    </v-row>
  </v-container>
</template>
<!--
  <template v-else>
  <h1>Hittar inte filmen</h1>
</template>
-->
</div>
</template>

<script>

export default {
  name: 'search',

  components: {
      //
  },

  data: () => ({
    searchResult:{},
    loaded: false,
    input:"",

  }),
  methods:{
    nextpage(){
    },
    goTo(title){
      window.location.href = window.location.origin + "/movie/"+title;
    },
    search(){
      this.getSearch();
    },
    async getSearch() { 
      let url = window.location.origin + "/api/movie/search/?p=1&s="+this.input;
      let response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization:
            "Bearer "+this.$store.state.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        this.searchResult = await response.json();
        this.loaded = true;
      } else {
        this.loaded = false;
      }
    }
  },
};
</script>