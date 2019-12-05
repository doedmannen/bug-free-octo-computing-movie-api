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
              :counter="15"
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
    <v-btn @click="registerUser">Register</v-btn>
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
      v => v.length >= 5 || "Username must be atleast 5 characters",
      v => v.length <= 10 || "Username must be less than 15 characters"
    ],
    passwordRules: [
      v => !!v || "Password is required",
      v => v.length >= 5 || "Password must be atleast 5 characters"
    ],

  })
  ,

  methods: {

    async registerUser() {
      let result = await fetch('api/users', {
        method: 'POST',
        headers: {'Content-type' : 'application/json'},
        body: JSON.stringify({username: this.username, password: this.password})
      })

    result = await result.json()

    console.log(result)

    }

  }
};
</script>

<style scoped>
</style>