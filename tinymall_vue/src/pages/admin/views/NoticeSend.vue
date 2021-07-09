<template>
  <div class="notice-send-panel">
    <el-form label-position="left" :model="notice" ref="form" :rules="rules" label-width="80px" :inline="false" size="normal">
      <el-form-item label="标题">
        <el-input v-model="notice.noticeTitle"></el-input>
      </el-form-item>
      <el-form-item label="内容" size="normal">
        <el-input v-model="notice.content" placeholder="输入正文内容" :autosize="{minRows:4,maxRows:8}" size="normal" clearable type="textarea"></el-input>
      </el-form-item>
      <el-form-item label="发送给" size="normal">
        <div style="width: 100%; display: flex; justify-content: space-between">
          <el-select v-model="status" placeholder="请选择通知对象" clearable filterable @change="getUserId">
            <el-option label="购买过本店商品的顾客" value="finished"></el-option>
            <el-option label="未收货的顾客" value="unreceive"></el-option>
            <el-option label="未付款的顾客" value="unpaid"></el-option>
          </el-select>  
          <div style="width: fit-content; color: gray; margin: 0;">({{to.length}})</div>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">发送</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      notice: {},
      to: [],
      status: '',
    }
  },
  methods: {
    getUserId(status) {
      this.axios.get("/shop/user/"+status)
        .then(resp=>{
          if(resp.data.success) {
            this.to = resp.data.data;
          } else {
            this.$message.error(resp.data.message);
          }
        })
    },
    onSubmit() {
      this.axios.post("/notice/add",{
        notice: this.notice,
        to: this.to
      })
      .then(resp=>{
        this.$msg(resp.data.success,resp.data.message);
      })
    }
  }
}
</script>
<style lang="scss">
.notice-send-panel {
  width: 50%;
  margin: 10% auto;
  .el-form-item {
    width: 70%;
    margin-bottom: 50px;
  }
  .el-select {
    width: 90%;
  }
}
</style>