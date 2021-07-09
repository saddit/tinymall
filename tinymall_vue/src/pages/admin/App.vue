<template>
  <el-container :direction="horizontal">
    <el-aside width="210px">
      <el-menu
        mode="vertical"
        router
        :default-active="$route.path"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
      <template           
        v-for="(submenus, index) in menus"
        :key="index">
        <template v-if="submenus.path">
          <el-menu-item :index="submenus.path" :key="index">
             <template #title><i class="m-icon" :class="submenus.icon"></i>{{ submenus.title }}</template>
          </el-menu-item>
        </template>
        <template v-else>
          <el-submenu
            :key="index"
            :index="index">
            <template #title><i class="m-icon" :class="submenus.icon"></i>{{ submenus.title }}</template>
            <el-menu-item
              v-for="(item, subIndex) in submenus.menus"
              :index="item.path"
              :key="subIndex">
              {{ item.title }}
            </el-menu-item>
          </el-submenu>
        </template>
      </template>
      </el-menu>
    </el-aside>
    <el-container :direction="vertical">
      <el-header height="fit-content">
        <el-page-header
          style="padding: 20px"
          @back="goBack"
          :content="title"
        ></el-page-header>
      </el-header>
      <el-main height="">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
export default {
  data() {
    return {
      fromPath: "/",
      title: "",
      menus: [
        {
          title: "店铺管理",
          icon: "el-icon-s-shop",
          path: "/shopinfo",
        },
        {
          title: '订单管理',
          icon: "el-icon-s-order",
          path: '/orderlist'
        },
        {
          title: "商品管理",
          icon: "el-icon-goods",
          menus: [
            {
              title: "所有商品",
              path: "/productlist"
            },
            {
              title: "添加商品",
              path: "/product"
            }
          ]
        },
        {
          title: "活动管理",
          icon: "el-icon-news",
          menus: [
            {
              title: "活动列表",
              path: '/activitylist'
            },
            {
              title: "发布活动",
              path: '/activity'
            }
          ]
        },
        {
          title: "消息管理",
          icon: "el-icon-message",
          menus: [
            {
              title: "历史消息",
              path: "/noticelist"
            },
            {
              title: "发布消息",
              path: "/notice"
            }
          ]
        },
        {
          title: "数据统计",
          icon: "el-icon-s-marketing",
          path: "/report"
        },
      ],
    };
  },
  methods: {
    updateTitle(route) {
      this.title = route.meta.title;
    },
    goBack() {
      this.$router.push({ path: this.fromPath });
    },
  },
  watch: {
    $route(to, from) {
      if (!this.$store.state.roleId || this.$store.state.roleId != 3) {
        window.location = '/main';
      }
      this.updateTitle(to);
      this.fromPath = from.path;
    },
  }
};
</script>
<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
html,body {
  margin: 0;
  padding: 0;
  height: 100%;
}
#app,.box {
  height: 100%;
}
.el-container {
  height: 100%;
}

.el-aside {
  overflow-y: scroll;
  .el-menu {
    height: 100%;
  }
}

.m-icon {
  margin-right: 10px;
  font-size: 18px;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
