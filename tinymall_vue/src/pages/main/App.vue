<template>
  <el-container direction="vertical">
    <el-affix :offset="0">
    <el-header style="background: white;" height="fit-content">
      <el-row type="flex" justify="space-between" align="middle">
        <el-col :span="3">
          <el-image :src="require('@/assets/logo_main.png')" fit="fill" :lazy="true" @click="$router.push('/')"></el-image>
        </el-col>
        <el-col :span="4">
          <el-menu mode="horizontal" :default-active="$route.path" router>
            <el-menu-item index="/index" key="0">首页</el-menu-item>
            <el-menu-item index="/event" key="1">活动</el-menu-item>
            <el-menu-item index="/region" key="2">专区</el-menu-item>
            <el-menu-item index="/search" key="3">搜索</el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="12">
          <el-input @change="toSearch" style="width: 45vw;" v-model="keyword" placeholder="搜索商品" clearable prefix-icon="el-icon-search"/>
        </el-col>
        <el-col :span="2" :offset="2">
          <div class="right-coner-btn-group" v-if="isLogin()">
            <el-button icon="el-icon-shopping-cart-1" circle @click="$router.push('/shopcar')"></el-button>
            <el-badge :value="noReadNoticeCount" :max="99" :is-dot="false" :hidden="noReadNoticeCount == 0" type="primary">
              <el-button icon="el-icon-user" circle @click="$router.push('/userinfo')"></el-button>     
            </el-badge>
          </div>
          <div v-else class="right-coner-btn-group">
            <el-button type="text" @click="goLogin">登录 & 注册</el-button>
          </div>
        </el-col>
      </el-row>
    </el-header>
    </el-affix>
    <el-container direction="vertical">
      <el-main height="100%">
        <!-- Main content -->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
const anonymousRouter = ['/index','/','/event','/search','/region','/product']
export default {
  data() {
    return {
      keyword: '',
      noReadNoticeCount: 0,
    }
  },
  mounted() {
    this.getNoReadNoticeCount();
    this.getNewestNotice();
  },
  methods: {
    getNoReadNoticeCount() {
      this.axios.get('/notice/count').then(resp=>{
        if(resp.data.success) {
          this.noReadNoticeCount = resp.data.data;
        }
      })
    },
    getNewestNotice() {
      this.axios.post('/notice/query/page',{
        dto: {
          isRead: false,
        },
        page: {
          pageNum:1,
          pageSize:1,
          orderBy: 'create_time'
        }
      }).then(resp=>{
        if(resp.data.success && resp.data.data.list.length >0) {
          this.$notify({
            title: resp.data.data.list[0].noticeTitle,
            message: resp.data.data.list[0].content,
            duration: 0,
            offset: 100,
            onClose:()=>{
              this.axios.get('/notice/read/'+resp.data.data.list[0].noticeId);
            }
          })
        }
      })
    },
    isLogin() {
      return this.$store.state.token != null
    },
    goLogin() {
      window.location = '/login'
    },
    toSearch() {
      if(this.keyword) {
        this.$router.push('/search?keyword='+this.keyword);
      }
    }
  },
  watch: {
    $route(to) {
      if(anonymousRouter.indexOf(to.path) == -1 && !this.$store.state.roleId) {
        window.location = '/login'
      }
    },
  }
}
</script>
<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0;
}
.el-pagination {
  margin-top: 30px;
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

.right-coner-btn-group {
  display: flex;
  justify-content: space-around;
}

.bottom-bar {
  width: 100%;
  height: fit-content;
  background: #fff1ec;
  padding-top: 10px;
  padding-bottom: 10px;
}
</style>
