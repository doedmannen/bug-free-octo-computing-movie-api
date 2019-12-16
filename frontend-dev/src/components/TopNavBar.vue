<template>
  <div>
    <v-toolbar dark color="blue-grey darken-3">
      <v-toolbar-title>Much movie such night</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-toolbar-items>
        <v-btn to="/" text>Home</v-btn>
        <v-btn to="/about" text>About</v-btn>
        <v-btn to="/search" text>Search</v-btn>
        <v-btn to="/createEvent" text>Create movie night</v-btn>
      </v-toolbar-items>

      <template v-if="$vuetify.breakpoint.smAndUp">
        <v-btn icon>
          <v-icon>mdi-export-variant</v-icon>
        </v-btn>
        <v-btn icon>
          <v-icon>mdi-delete-circle</v-icon>
        </v-btn>
        <v-btn icon>
          <v-icon>mdi-plus-circle</v-icon>
        </v-btn>
      </template>
    </v-toolbar>
  </div>
</template>

<script>
export default {
  name: "TopNavBar",

  data: () => ({})
};
</script>