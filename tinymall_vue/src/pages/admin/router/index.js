import { createRouter, createWebHashHistory } from 'vue-router'
import Index from '../views/ShopInfo.vue'

const routes = [
  {
    path: '/',
    redirect: '/shopinfo'
  },
  {
    path: '/shopinfo',
    name: 'shopinfo',
    component: Index,
    meta: {
      title: '我的店铺'
    }
  },
  {
    path: '/productlist',
    name: 'productlist',
    component: () => import("../views/ProductList.vue"),
    meta: {
      title: '我的商品'
    }
  },
  {
    path: '/product',
    name: 'product',
    component: () => import('../views/Product.vue'),
    meta: {
      title: "商品信息"
    }
  },
  {
    path: '/notice',
    name: 'notice',
    component: ()=>import('../views/NoticeSend.vue'),
    meta: {
      title: "发送消息",
    }
  },
  {
    path: '/noticelist',
    name: 'noticelist',
    component: ()=>import('../views/NoticeList.vue'),
    meta: {
      title: "历史消息",
    }
  },
  {
    path: '/orderlist',
    name: 'orderlist',
    component: ()=>import('../views/OrderList.vue'),
    meta: {
      title: "店铺订单",
    }
  },
  {
    path: '/order',
    name: 'order',
    component: ()=>import('../views/OrderDetail.vue'),
    meta: {
      title: "订单详情",
    },
  },
  {
    path: '/activitylist',
    name: 'activitylist',
    component: ()=>import('../views/ActivityList.vue'),
    meta: {
      title: "历史活动"
    }
  },
  {
    path: '/activity',
    name: 'activity',
    component:()=>import('../views/Activity.vue'),
    meta: {
      title: "活动信息"
    }
  },
  {
    path: '/report',
    name: 'report',
    component:()=>import('../views/Report.vue'),
    meta: {
      title: "数据统计"
    }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
