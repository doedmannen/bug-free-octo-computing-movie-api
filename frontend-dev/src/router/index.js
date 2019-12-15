import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('@/views/About.vue')
  },
  {
    path:'/login',
    name: 'login',
    component: () => import('@/views/Login.vue')
  },
  {
    path:'/register',
    name: 'register',
    component: () => import('@/views/Register.vue')
  },
  {
    path:'/search',
    name: 'search',
    component: () => import('@/views/Search.vue')
  },
  {
    path:'/movie/:title',
    name: 'movie',
    component: () => import('@/views/Movie.vue')
  },
  {
    path:'/createEvent',
    name: 'createEvent',
    component: () => import('@/views/CreateEvent.vue')
  },
  {
    path:'/log',
    name: 'log',
    component: () => import('@/views/Log.vue')
  },
  {
    path: '*', 
    name: '404',
    component: () => import('@/views/missing-page.vue')
  }
]

const router = new VueRouter({
  base: '/',
  mode: 'history',
  routes
})

export default router
