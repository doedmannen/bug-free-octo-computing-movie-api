<template>
  <div class="login">
    <v-row justify="center">
        
        <v-col cols="4"> 
          <h4> This is a site where you can book movie nights with your friends.
      Search and read about films. </h4>
        </v-col>
      </v-row>
      <v-row justify="center">
        
        <v-col cols="4"> 
          <h1>Login</h1>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="4">
            <v-form
              ref="form"
              v-model="valid"
              lazy-validation 
            >

            <v-text-field
              v-model="username"
              :counter="15"
              :rules="usernameRules"
              label="Username"
              required
            ></v-text-field>

            <v-text-field
              v-model="password"
              :rules="passwordRules"
              type="password"
              label="Password"
              required
            ></v-text-field>

            <v-btn
              :disabled="!valid"
              color="success"
              class="mr-4"
              @click="validate"
            >
              Login
            </v-btn>

            <v-btn
              color="error"
              class="mr-4"
              @click="reset"
            >
              Clear
            </v-btn>
          </v-form>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="4"> 
         <router-link to="/register">Don't have an account? Click here to register for one</router-link> 
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>
<script>
  export default {
    name: 'login',
    data: () => ({
      valid: true,
      username: '',
      usernameRules: [
        v => !!v || 'Username is required',
        v => (v && v.length <= 15) || 'Username must be less than 15 characters',
      ],
      password: '',
      passwordRules: [
        v => !!v || 'Password is required'
      ],
      loginRespose: ""
    }),
    computed: {
      hasValidAuth(){
        if(this.$store.token)
          return true;
        return false; 
      }
    },
    methods: {
      async validate () {
        this.loginResponse = "";
        if (this.$refs.form.validate()) {
          let response = await fetch(window.location.origin + "/api/auth/login", {
            method: "POST", 
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify({
              username: this.username, 
              password: this.password
            })
          });
          if(response.status === 200){
            let jwt = await response.json();
            this.$store.commit("setToken", jwt.token)
            this.$router.push({path: "/"});
          } else {
            this.loginResponse = "Username and/or password was incorrect";
          }
        }
      },
      reset () {
        this.loginResponse = ""; 
        this.$refs.form.reset()
      }
    },
    beforeMount(){
      this.$store.dispatch("destroyToken"); 
    }
  }
</script>
<style scoped>

</style>
