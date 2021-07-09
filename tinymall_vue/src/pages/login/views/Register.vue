<template>
  <div>
    <el-form :model="regForm" ref="form" :rules="rules" label-width="fit-content" :inline="false" size="normal">
      <el-form-item label="账号" required>
        <el-row gutter="20">
          <el-col :span="12">
            <el-popover trigger="focus" width="fit-content" content="由英文数字组成" placement="top-end">
              <template #reference>
                <el-input v-model="regForm.username" placeholder="用户名"></el-input>            
              </template>
            </el-popover>
          </el-col>
          <el-col :span="12">
            <el-popover
              placement="top-end"
              width="fit-content"
              trigger="focus"
              content="至少使用英文和数字组成">
              <template #reference>
                <el-input show-password v-model="regForm.password" placeholder="密码"/> 
              </template>
            </el-popover>
          </el-col>   
        </el-row>
      </el-form-item>
      <el-form-item label="邮箱" required>
        <el-input type="email" v-model="regForm.email" placeholder="输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="邮箱验证码" required>
        <el-row :gutter="1">
          <el-col :span="15" :offset="0"><el-input size="small" v-model="regForm.emailCode" placeholder="输入验证码"></el-input></el-col>
          <el-col :span="9" :offset="0"><el-button type="primary" size="small" @click="sendCode('email')">发送</el-button></el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="手机号" size="normal" required>
        <el-input type="tel" v-model="regForm.phone" placeholder="输入手机号码"></el-input>
      </el-form-item>
      <el-form-item label="手机验证码" required>
        <el-row :gutter="1">
          <el-col :span="15" :offset="0"><el-input size="small" v-model="regForm.phoneCode" placeholder="输入验证码"></el-input></el-col>
          <el-col :span="9" :offset="0"><el-button type="primary" size="small" @click="sendCode('phone')">发送</el-button></el-col>
        </el-row>
      </el-form-item>   
      <el-form-item>
        <el-button type="primary" @click="onSubmit">立即注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import Const from '@/js/const'
export default {
  data() {
    return {
      baseUrl: Const.baseUrl,
      regForm: {
        username: '',
        email: '',
        password: '',
        emailCode: '',
        phone: '',
        phoneCode: '',
      }
    }
  },
  methods: {
    onSubmit(){
      this.axios.post('/user/register',this.regForm).then(resp=>{
        if(resp.data.success) {
          this.$message.success(resp.data.message);
          this.$router.push('/login');
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    sendCode(option) {
      this.axios.get('/user/reg/'+option+'/'+this.regForm.email).then(resp=>{
        if(resp.data.success) {
          this.$message.success(resp.data.message);
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
  },
}
</script>
<style lang="scss">
</style>