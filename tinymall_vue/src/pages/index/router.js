import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/main'
  },
  {
    path: '/admin',
    beforeEnter() {
      window.location = "/admin"
    }
  },
  {
    path: '/main',
    beforeEnter() {
      window.location = "/main"
    }
  },
  {
    path: '/login',
    beforeEnter() {
      window.location = "/login"
    }
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
