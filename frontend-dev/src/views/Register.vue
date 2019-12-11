<template>
  <div class="register d-flex justify-center">
    <v-stepper class="stepper my-5 col-6" v-model="e1">
      <v-stepper-header>
        <v-stepper-step :complete="e1 > 1" step="1">User details</v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step :complete="e1 > 2" step="2">Link your email</v-stepper-step>
      </v-stepper-header>

      <v-stepper-items>
        <v-stepper-content step="1">
          <v-card class="mb-12" color="grey lighten-4" height="230px">
            <v-layout align-center fill-height>
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
                        type="Password"
                        label="Password"
                        required
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-form>
            </v-layout>
            <p
              class="text-center username-error mt-1"
              v-if="this.usernameExistError"
            >Username already exists, try another one.</p>
          </v-card>
          <v-btn color="primary" :disabled="!valid" @click="checkUsernameAvailability">Continue</v-btn>
        </v-stepper-content>

        <v-stepper-content step="2">
          <v-card class="mb-12" color="grey lighten-4" height="230px">
            <v-layout justify-center align-center fill-height>
              <v-btn v-if="this.showLinkEmailButton" @click="authGoogle">
                Link Google account
                <img
                  src="https://img.icons8.com/offices/30/000000/google-logo.png"
                />
              </v-btn>
              <div class="text-center" v-else>
                <h2 class="success-link-text">
                  Successfully linked your account
                  <v-icon class="check mb-1">mdi-check</v-icon>
                </h2>
                <p class="confirm-registration">Press register to complete your registration</p>
              </div>
            </v-layout>
          </v-card>
          <div class="d-flex justify-space-between">
            <v-btn color="primary" @click="e1 = 1">Back</v-btn>
            <v-btn color="success" :disabled="showLinkEmailButton" @click="registerUser">Register</v-btn>
          </div>
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
    showLinkEmailButton: true,
    usernameExistError: false,
    username: "",
    password: "",
    accessToken: "weeeo",
    refreshToken: "",
    expiresAt: "",

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
    async checkUsernameAvailability() {
      this.usernameExistError = false;

      let result = await fetch("api/users/check-available", {
        method: "POST",
        headers: { "Content-type": "application/json " },
        body: JSON.stringify({
          username: this.username
        })
      });

      //result = await result;
      console.log(result);
      if (result.status === 200) {
        this.e1 = 2;
      } else if (result.status === 409) {
        this.usernameExistError = true;
      }
    },

    async registerUser() {
      let response = await fetch("api/users/", {
        method: "POST",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify({
          username: this.username,
          password: this.password,
          accessToken: this.accessToken,
          refreshToken: this.refreshToken,
          expiresAt: this.expiresAt
        })
      });

      response = await response.json();

      if (response.status === 200) {
        this.$router.push({ path: "/login" });
      } else if (response.status === 409) {
        this.e1 = 1;
        this.usernameExistError = true;
      }
    },

    authGoogle() {
      window.auth2.grantOfflineAccess().then(this.signInCallback);
    },

    async signInCallback(authResult) {
      console.log("authResult", authResult);

      if (authResult["code"]) {
        let response = await fetch("api/googleauth/storeauthcode", {
          method: "POST",
          headers: {
            "Content-Type": "application/octet-stream; charset=utf-8",
            "X-Requested-With": "XMLHttpRequest"
          },
          body: authResult["code"]
        });

        if (response.status === 200) {
          this.showValidEmailLinkMessage = true;
          this.showLinkEmailButton = false;

          let result = await response.json();
          this.accessToken = result.access_token;
          this.refreshToken = result.refresh_token;
          this.expiresAt = result.expires_in;
        }
      } else {
        // There was an error.
      }
    }
  }
};
</script>

<style scoped>
.username-error {
  color: rgb(173, 14, 14);
}

.success-link-text,
.confirm-registration {
  color: rgba(0, 0, 0, 0.664);
}

.check {
  color: rgb(37, 134, 37);
}
</style>