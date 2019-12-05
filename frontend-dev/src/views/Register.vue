<template>
  <div class="register">
    <h1>This is the register page</h1>
    <v-form v-model="valid">
      <v-container>
        <v-row>
          <v-col cols="12" md="12">
            <v-text-field
              v-model="username"
              :rules="nameRules"
              :counter="12"
              label="Username"
              required
            ></v-text-field>
          </v-col>

          <v-col cols="12" md="12">
            <v-text-field v-model="password" :rules="passwordRules" label="Password" required></v-text-field>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
    <v-btn>Register</v-btn>
  </div>
</template>


<script>
export default {
  name: "register",

  components: {
    //
  },

  data: () => ({
    valid: false,
    username: "",
    password: "",
    nameRules: [
      v => !!v || "Username is required",
      v => v.length <= 10 || "Username must be less than 10 characters"
    ],
    passwordRules: [
      v => !!v || "Password is required",
      v => v.length > 2 || "Password must be more then 2 characters"
    ]
  })
};
</script>

<style scoped>
</style>