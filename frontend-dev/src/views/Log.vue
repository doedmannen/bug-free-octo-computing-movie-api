<template>
  <div class="log">
    <h1>Log</h1>
    <v-row v-if="loaded && exist">
      <div v-for="(value) in log" :key="value.when">
        <v-col>
          <br>
          when:{{chancgetime(value.when)}}
          url:{{value.url}}
          query:{{value.query}}
          status:{{value.statuscode}}
          ip:{{value.ip}}
        </v-col>
      </div>
      <div v-if="loaded && exist">
        <br>
           <v-btn @click="previouspage" :disabled="page === 1">previous</v-btn>
           {{page}}
          <v-btn @click="nextpage" :disabled="this.max < 50">Next</v-btn>
        </div>
    </v-row>
    <div v-else-if="loaded && !exist">
       <h3>Lodded</h3>
    </div>
  <div v-else>
       <h3>Can´t find any</h3>
    </div>
  </div>
</template>

<script>
export default {
  name: "log",

  data: () => ({
    log: [],
    loaded: false,
    exist:false,
    page: 1,
    max:''
  }),
  mounted() {
    this.getLog();
  },

  methods: {
    chancgetime(t) {
      let d = new Date(t);
      return d.toLocaleString();
    },nextpage(){
    this.page++
    this.getLog();
    },
    previouspage(){
      this.page--
    this.getLog();
    },getmaxpage(){
    this.max = this.log.length
    },
    async getLog() {
      let url = window.location.origin + "/api/log?page="+this.page;
      let response = await fetch(url, {
        method: "GET",
        headers: {
          Authorization: "Bearer " + this.$store.state.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        this.log = await response.json();
        this.exist = true;
        this.getmaxpage();
      }else if(response.status === 403){
          this.$router.push({
              path:'/'
          })
      }
       else {
        this.exist = false;
      }
      this.loaded = true;
    }
  }
};
</script>
<style scoped>
/** */
</style>