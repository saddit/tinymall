<template>
  <div id="main">
    <el-form :model="loginForm" ref="form" :rules="rules" label-width="0" :inline="false" size="normal">
      <el-form-item>
        <el-input v-model="loginForm.username" placeholder="输入手机、邮箱或用户名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input show-password v-model="loginForm.password" placeholder="输入密码"></el-input>
      </el-form-item>
      <el-form-item class="verify-fi">
        <el-input style="width: 60%;" v-model="loginForm.verifyCode" placeholder="输入验证码" size="normal" clearable></el-input>
        <el-image style="margin: 0 10px;" @click="getVerifyCodeImg" :src="verifyCodeImg" fit="fit" :lazy="true"></el-image>
      </el-form-item>
      <el-form-item>
        <el-checkbox v-model="loginForm.isRememberMe" :label="true" :indeterminate="false">记住密码</el-checkbox>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import Const from '@/js/const'
export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        verifyCode: '',
        isRememberMe: false,
      },
      verifyCodeImg: '',
    }
  },
  mounted() {
    this.getVerifyCodeImg();
  },
  methods: {
    onSubmit() {
      this.axios.post("/auth/login", this.loginForm)
        .then(resp=>{
          if(resp.data.success) {
            this.$store.commit('setUserInfo',resp.data.data);
            window.location = '/main'
          } else {
            this.$message.error(resp.data.message);
          }
        })
        .catch(error=>{
          console.log(error)
          this.$message.error(error);
        })
    },
    getVerifyCodeImg() {
      this.verifyCodeImg = Const.baseUrl + '/auth/validateCode/80/35?t=' + Date.now();
    }
  },
}
</script>
<style lang="scss">
#main {
  height: 100%;
  width: 100%;
  margin: 0 auto;
  .el-form-item {
    margin: 20px 25px;
  }
  .verify-fi {
    .el-form-item__content {
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }
  }
}
</style>