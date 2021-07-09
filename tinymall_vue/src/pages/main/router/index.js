import { createRouter, createWebHashHistory } from 'vue-router'
import Index from '../views/Index.vue'

const routes = [
  {
    path: '/',
    redirect: '/index'
  },
  {
    path: '/index',
    name: 'index',
    component: Index
  },
  {
    path: '/region',
    name: 'region',
    component: () => import('../views/Region.vue')
  },
  {
    path: '/event',
    name: 'event',
    component: () => import('../views/Event.vue')
  },
  {
    path: '/search',
    name: 'search',
    component: () => import('../views/Search.vue')
  },
  {
    path: '/userinfo',
    name: 'userinfo',
    component: () => import('../views/UserInfo.vue')   
  },
  {
    path: '/shopcar',
    name: 'shopcar',
    component: () => import('../views/ShopCar.vue')   
  },
  {
    path: '/ordersettle',
    name: 'ordersettle',
    component: ()=> import('../views/OrderSettle.vue')
  },
  {
    path: '/orderlist',
    name: 'orderlist',
    component: ()=> import('../views/OrderList.vue')
  },
  {
    path: '/collection',
    name: 'collection',
    component: ()=> import('../views/Collection.vue')
  },
  {
    path: '/address',
    name: 'address',
    component: ()=> import('../views/Address.vue')
  },
  {
    path: '/product',
    name: 'product',
    component: ()=>import('../views/ProductDetail.vue')
  },
  {
    path: '/noticelist',
    name: 'noticelist',
    component: ()=>import('../views/NoticeList.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
