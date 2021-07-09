<template>
  <el-container>
    <el-header height="5%" style="text-align: left;">
      <el-image :src="require('@/assets/logo_main.png')" @click="goMain"/>
    </el-header>
    <el-main>
      <el-card class="body-card" shadow="always" :body-style="{ padding: '20px' }">
        <template #header>
          <span id="title">{{$route.meta.title}}</span>
        </template>
        <router-view />
        <div id="footer">
          <router-link v-if="$route.path == '/login'" to="/register"><el-link type="primary">没有账号？点击立即注册</el-link></router-link>
          <router-link v-if="$route.path == '/register'" to="/login"><el-link type="primary">已有账号？点击立即登录</el-link></router-link>
        </div>
      </el-card>
    </el-main>
    <el-footer height="fit-content">
      <div class="info-footer">
        <p>施嘉宏 2021年春 电商系统 课设</p>
      </div>
    </el-footer>
  </el-container>
</template>
<script>
export default {
  methods: {
    goMain() {
      window.location = '/main'
    }
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
}
// .el-main {
//   background-image: url('~@/assets/login_back.jpg');
//   background-size: cover;
// }
body {
  margin: 0;
}
.body-card {
  padding: 20px;
  height: fit-content;
  width: fit-content;
  margin-top: 3%;
  margin-bottom: 3%;
  margin-left: 60%;
  margin-right: 10%;
}
#title {
  color: #ff5000;
  font-size: 50px;
  margin: 0 auto;
}
#footer {
  margin: 0 auto;
  margin-top: 25px;
}
.info-footer {
  margin: 0 auto;
  padding-top: 75px;
  .p{
    color: gray;
  }
}
</style>
