<template>
  <div class="info-panel">
    <el-form label-position="left" :model="shopInfo" label-width="100px" :inline="false" size="normal">
      <el-form-item label="商店图标">
        <el-upload
          class="avatar-uploader"
          :action="baseUrl + '/resource/img/upload'"
          :show-file-list="false"
          with-credentials
          :on-success="handleIconSuccess">
          <el-image v-if="shopInfo.shopIcon" :src="shopInfo.shopIcon" fit="fill" class="avatar"/>
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="商店名" size="normal">
        <el-input v-model="shopInfo.shopName" size="normal"></el-input>
      </el-form-item>
      <el-form-item label="简介" size="normal">
        <el-input v-model="shopInfo.shopDesc" 
        placeholder="简单介绍你的店铺吧"
         size="normal" clearable 
         type="textarea" 
         :autosize="{ minRows: 2, maxRows: 4}" />
      </el-form-item>
      <el-form-item label="微信交易号" size="normal">
        <el-input v-model="shopInfo.wepayId" placeholder="微信支付交易号" size="normal"></el-input>
      </el-form-item>
      <el-form-item label="支付宝交易号" size="normal">
        <el-input v-model="shopInfo.alipayId" placeholder="支付宝交易账号" size="normal"></el-input>
      </el-form-item>
      <el-form-item label="店铺类型" size="normal">
        <el-select v-model="shopInfo.categoryId">
          <el-option v-for="item in categories"
            :key="item.categoryId"
            :label="item.categoryDesc"
            :value="item.categoryId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="参加的活动" size="normal">
        <el-input  v-model="activity.activityTitle" placeholder="没有参加任何活动" readonly></el-input>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="updateShopInfo">提交更改</el-button>
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
      shopInfo: {},
      categories: [],
      activity: {},
    }
  },
  mounted() {
    this.getCategroies();
    this.getShopInfo();
  },
  methods: {
    getCategroies() {
      this.axios.get('/category/all')
        .then(resp => {
          if(resp.data.success) {
            this.categories = resp.data.data;
          }
        })
    },
    handleIconSuccess(res) {
      this.shopInfo.shopIcon = res.data;
    },
    getShopInfo() {
      this.axios.get('/shop/user').then(resp=>{
        if(resp.data.success) {
          this.shopInfo = resp.data.data;
          this.getActivity(this.shopInfo.activityId);
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    getActivity(aid) {
      if(!aid) return;
      this.axios.get('/activity/query/'+aid).then(resp=>{
        if(resp.data.success) {
          this.activity = resp.data.data;
        }
      })
    },
    updateShopInfo() {
      this.axios.post('/shop/update',this.shopInfo).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
      })
    }
  }
}
</script>

<style lang="scss" scope>
.avatar-uploader {
  width: 100px;
  height: 100px;
  .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      line-height: 100px;
      text-align: center;
  }
  .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 100px;
    height: 100px;  
  }
  .avatar {
    width: 100px;
    height: 100px;
  }
}
.info-panel {
  width: 60%;
  margin: 5% auto;
  .el-form-item {
    width: 50%;
    margin-top: 50px;
    .el-select {
      width: 100%;
    }
  }
}
</style>
