import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';

Vue.config.productionTip = false

router.beforeEach(async (to, from, next) => {
  await store.dispatch('tokenLookup');
  let token = store.state.token || localStorage.token; 
  let publicPaths = ['start','login', 'register'];
  let restrictedAccess = !publicPaths.includes(to.name); 

  if(!token && restrictedAccess){
    return next('/start');
  }

  next(); 
})

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
