<template>
  <div>
    <v-toolbar dark color="blue-grey darken-3">
      <div>
        <v-img src="@/assets/doge.png" width="67px"></v-img>
      </div>
      <v-toolbar-title>Much movie such night</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-toolbar-title>{{ $store.state.token ? $store.state.username : "" }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items>
        <v-btn to="/" text>Home</v-btn>
        <v-btn to="/about" text>About</v-btn>
        <v-btn to="/search" text>Search</v-btn>
        <v-btn v-if="$store.state.isAdmin" to="/log" text>Log</v-btn>
        <v-btn to="/createEvent" text>Create movie night</v-btn>
        <v-btn to="/login" text>{{ $store.state.token ? "logout" : "login" }}</v-btn>
      </v-toolbar-items>
    </v-toolbar>
  </div>
</template>

<script>
export default {
  name: "TopNavBar",

  data: () => ({})
};
</script>
