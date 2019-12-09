<template>
  <div class="register d-flex justify-center">
    <v-stepper class="stepper my-5 col-10" v-model="e1">
      <v-stepper-header>
        <v-stepper-step :complete="e1 > 1" step="1">User details</v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step :complete="e1 > 2" step="2">Link your email</v-stepper-step>
      </v-stepper-header>

      <v-stepper-items>
        <v-stepper-content step="1">
          <v-card class="mb-12" color="grey lighten-4" height="200px">
            <v-form v-model="valid">
              <v-container class="py-0">
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
                    <v-text-field
                      v-model="password"
                      :rules="passwordRules"
                      label="Password"
                      required
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-card>
          <v-btn color="primary" @click="e1 = 2">Continue</v-btn>
        </v-stepper-content>

        <v-stepper-content step="2">
          <v-card class="mb-12" color="grey lighten-4" height="200px">
            <v-btn @click="authGoogle">Link Google account</v-btn>
          </v-card>
          <v-btn color="primary" @click="e1 = 1">Back</v-btn>
          <v-btn @click="registerUser">Register</v-btn>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
  </div>
</template>


<script>
export default {
  name: "register",

  components: {
    //
  },

  data: () => ({
    e1: 0,
    valid: false,
    username: "",
    password: "",
    nameRules: [
      v => !!v || "Username is required",
      v => v.length >= 5 || "Username must be atleast 5 characters",
      v => v.length <= 15 || "Username must be less than 15 characters"
    ],
    passwordRules: [
      v => !!v || "Password is required",
      v => v.length >= 5 || "Password must be atleast 5 characters"
    ]
  }),
  methods: {
    async registerUser() {
      let result = await fetch("api/users/", {
        method: "POST",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify({
          username: this.username,
          password: this.password
        })
      });

      result = await result.json();

      console.log(result);
    },

    authGoogle() {
      window.auth2.grantOfflineAccess().then(this.signInCallback);
    },

    async signInCallback(authResult) {
      console.log("authResult", authResult);

      // if (authResult["code"]) {
      //   // Hide the sign-in button now that the user is authorized
      //   // $("#signinButton").hide();

      //   // Send the code to the server
      //   let result = await fetch("/storeauthcode", {
      //     method: "POST",
      //     headers: {
      //       "Content-Type": "application/octet-stream; charset=utf-8",
      //       "X-Requested-With": "XMLHttpRequest"
      //     },
      //     body: authResult["code"]
      //   });
      //   // etc...
      // } else {
      //   // There was an error.
      // }
    }
  }
};
</script>

<style scoped>
/* .register{
  justify-content: center;
  display: flex;
} */
</style>