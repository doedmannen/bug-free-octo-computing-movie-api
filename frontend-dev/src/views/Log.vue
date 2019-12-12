<template>
  <div class="log">
    <h1>Log</h1>
    <v-row v-if="loaded && exist">
      <div v-for="(value) in log" :key="value.when">
        <v-col>
          När:{{chancgetime(value.when)}}
          url:{{value.url}}
          query:{{value.query}}
          status:{{value.statuscode}}
          ip:{{value.ip}}
        </v-col>
      </div>
    </v-row>
    <div v-else-if="loaded && !exist">
       <h3>Ladar</h3>
    </div>
  <div v-else>
       <h3>Hittar inga</h3>
    </div>
  </div>
</template>

<script>
export default {
  name: "log",

  data: () => ({
    log: [],
    loaded: false,
    exist:false
  }),
  mounted() {
    this.getLog();
  },

  methods: {
    chancgetime(t) {
      let d = new Date(t);
      return d.toLocaleString();
    },
    async getLog() {
      let url = window.location.origin + "/api/log";
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
      }else {
        this.exist = false;
      }
      this.loaded = true;
    }
  }
};
</script>
<style scoped>
</style>