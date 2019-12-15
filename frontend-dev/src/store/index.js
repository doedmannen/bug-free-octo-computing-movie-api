import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: null,
    username: null
  },
  mutations: {
    setToken(state, value){
      if(value && value.split(".")[1]){
        localStorage.setItem("token", value); 
        state.token = value; 
        state.username = JSON.parse(atob(value.split(".")[1])).sub;
      } else {
        state.username = null; 
        state.token = null; 
        delete localStorage.token;
      }
    }
  },
  actions: {
    async tokenLookup(context){
      let token = context.state.token || localStorage.getItem("token"); 
      if(token) {
        let isValid = false; 
        try{
          isValid = (Date.now() + 300000) < JSON.parse(atob(token.split(".")[1])).exp * 1000;
        } catch (err) {
          context.commit("setToken", null);
          delete localStorage.token; 
          return; 
        }
        if(isValid) {
          context.commit('setToken', token)
          return; 
        }
        let response = await fetch(window.location.origin + "/api/auth/refresh",
          {
            method: "GET",
            headers: {
              "Authorization": 'Bearer ' + token
            }
          }
        );
        if(response.status === 200){
          response = await response.json();
          context.commit("setToken", response.token);
          context.state.token = token; 
        } else {
          context.commit('setToken', null);  
        }
      }
    },
    destroyToken(context){
      context.state.token = null; 
      delete localStorage.token; 
    }
  },
  modules: {
  }
})
