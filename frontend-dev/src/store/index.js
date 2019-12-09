import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: null
  },
  mutations: {
    setToken(state, value){
      localStorage.setItem("token", value); 
      state.token = value; 
    }
  },
  actions: {
    tokenLookup(context){
      let token = localStorage.getItem("token"); 
      if(token && Date.now() < JSON.parse(atob(token.split(".")[1])).exp*1000){
        context.state.token = token; 
      } else {
        context.state.token = null; 
        delete localStorage.token; 
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
