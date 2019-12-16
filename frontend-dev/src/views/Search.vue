<template>
<div>
<template>
<v-container fluid>
    <v-row>
      <div class="search">
        <v-col cols="12" >
        <v-text-field label="Search" v-model="input" :rules="mincharacters"></v-text-field>
        <v-btn @click="search">Search</v-btn>
        <div v-if="loaded && exist">
           <v-btn @click="previouspage" :disabled="page === 1">previous</v-btn>
           {{page}}/
           {{max}}
          <v-btn @click="nextpage" :disabled="page === this.max">Next</v-btn>
        </div>
        <v-row v-if="searchResult.result">
             <div v-for="(value) in searchResult.result.Search" :key="value.imbdID">
              <p>{{value.Title}}</p>
              <v-col cols="8">
                <v-img :src="value.Poster === 'N/A' ? `https://image.shutterstock.com/image-vector/no-image-available-icon-template-260nw-1036735678.jpg` : value.Poster" @click="goTo(value.Title)"></v-img>
          </v-col>
          <p>{{value.Year}}</p>
            </div>
        </v-row>
         <div v-if="!exist && loaded">
            <h1>Can`t find film</h1>
        </div>
        </v-col>
      </div>
    </v-row>
  </v-container>
</template>
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
    exist: false,
    input:"",
    page: 1,
    max:0,
    mincharacters: [
      v => v.length >= 3 || 'Min 3 tecken'
      ],
  }),
  methods:{
    nextpage(){
    this.page++
    this.getSearch();
    },
    previouspage(){
      this.page--
    this.getSearch();
    },
    getmaxpage(){
    this.max = this.searchResult.result.totalResults
    this.max= this.max/10
    this.max= Math.ceil(this.max)
    },
    goTo(title){
      window.location.href = window.location.origin + "/movie/"+title;
    },
    search(){
      this.getSearch();
    },
    async getSearch() { 
      let url = window.location.origin + "/api/movie/search/?p="+this.page+"&s="+this.input;
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
        this.exist = true;
        this.getmaxpage();
      } else {
        this.exist = false;
      }
       this.loaded = true;
    }
  },
};
</script>