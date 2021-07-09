<template>
  <div class="activity-edit">
    <el-form :model="activity" label-width="80px" :inline="false" size="normal" label-position="left">
      <el-form-item label="预览图">
        <el-upload
          class="avatar-uploader"
          :action="baseUrl + '/resource/img/upload'"
          :show-file-list="false"
          with-credentials
          :on-success="uploadImgSuccess">
          <el-image v-if="activity.previewImg" :src="activity.previewImg" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload> 
      </el-form-item>
      <el-form-item label="标题" size="normal" required>
        <el-input v-model="activity.activityTitle" placeholder="输入活动标题" size="normal" clearable :max="15"></el-input>
      </el-form-item>
      <el-form-item label="内容" size="normal" required>
        <el-input v-model="activity.content" placeholder="活动内容介绍" size="normal" clearable type="textarea" :autsize="{maxRows:4,minRows:3}"></el-input>
      </el-form-item>
      <el-form-item label="截止时间(选填)" size="normal">
        <el-date-picker class="w-100p" v-model="activity.expiredTime" type="date" size="normal" placeholder="选择活动截止日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="活动页面" size="normal">
        <el-upload
          class="upload-html"
          :action="baseUrl + '/resource/html/upload'"
          :on-success="uploadHtmlSuccess"
          :on-error="(error)=>$(false,error.message)"
          :file-list="htmlFile"
          :limit="1"
          :accept="['html']"
          with-credentials
          list-type="text">
        <el-button size="small">点击上传</el-button>
          <template #tip>
            <div class="el-upload__tip">
              只能上传html文件
            </div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="优惠券" size="normal">
        <el-button type="primary" size="small" @click="couponDialog = true; newCoupon = {};">添加优惠券</el-button>
        <div class="coupon-box" v-if="coupons.length > 0">
          <coupon-view 
          v-for="cou,i in coupons" :key="i"
          btn-label="移除"
          :coupon="cou"
          @btn-click="removeCoupon(cou.couponId,i)"
          />
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">提交</el-button>
      </el-form-item>
    </el-form>   
  </div>
  <el-dialog
    title="添加优惠券"
    v-model="couponDialog"
    width="30%">
    <div style="width: 80%; margin: 0 auto;">
      <el-form label-position="left" :model="newCoupon" ref="form" :rules="rules" label-width="80px" :inline="false" size="normal">
        <el-form-item label="类型" required>
          <el-radio-group v-model="newCoupon.couponType" size="normal" required>
            <el-radio :label="0">
              满减
            </el-radio>
            <el-radio :label="1">
              百分比
            </el-radio>            
          </el-radio-group>
        </el-form-item>
        <hint-form-item label="梯度" hint="满足该值则优惠一次" required>
          <el-input-number v-model="newCoupon.couponStep" size="normal"
              :min="0" :step="1" :controls="true" controls-position="right">
          </el-input-number>
        </hint-form-item>
        <hint-form-item label="折扣值" hint="满减金额或折扣百分比(%)" required>
          <el-input-number v-model="newCoupon.couponValue" size="normal"
            :min="0" :step="1" :controls="true" controls-position="right">
          </el-input-number>
        </hint-form-item>
        <hint-form-item label="数量" required>
          <el-input-number :disabled="unlimitCount" v-model="newCoupon.count" size="normal"
            :min="-1" :step="1" :controls="true" controls-position="right">
          </el-input-number>
          <template #hint>
            <el-checkbox v-model="unlimitCount" :label="true" :indeterminate="false" @change="unlimit('count')">无限量</el-checkbox>
          </template>
        </hint-form-item>
        <hint-form-item label="领取限制" required>
          <el-input-number :disabled="unlimitUser" v-model="newCoupon.userLimit" size="normal"
            :min="-1" :step="1" :controls="true" controls-position="right">
          </el-input-number>
          <template #hint>
            <el-checkbox v-model="unlimitUser" :label="true" :indeterminate="false" @change="unlimit('user')">无限量</el-checkbox>
          </template>
        </hint-form-item>
        <el-form-item style="text-align: left;" size="normal">
          <el-checkbox v-model="newCoupon.applyMulti" :label="true" :indeterminate="false">优惠多次</el-checkbox>
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <el-button @click="closeCouponDialog">取消</el-button>
      <el-button type="primary" @click="addNewCoupon">确认</el-button>
    </template>
  </el-dialog>
</template>
<script>
import CouponView from '../components/CouponView.vue';
import HintFormItem from '../components/HintFormItem.vue'
import Const from '@/js/const'
export default {
  components: {HintFormItem, CouponView},
  data() {
    return {
      baseUrl: Const.baseUrl,
      activity: {
      },
      isUpdate: false,
      couponDialog: false,
      htmlFile: [],
      coupons: [],
      newCoupon: {},
      unlimitUser: false,
      unlimitCount: false,
    }
  },
  mounted() {
    var aid = this.$route.query.aid;
    if(aid) {
      this.getActivity(aid);
      this.isUpdate=true;
    }
  },
  methods: {
    getActivity(aid) {
      this.axios.get("/activity/query/"+aid)
      .then(resp=>{
        if(resp.data.success) {
          this.activity = resp.data.data;
          this.coupons = this.activity.coupons;
          if(this.activity.activityPage) {
            this.htmlFile.push({
              name: 'activity_page.html',
              url: this.activity.activityPage
            })
          }
        } else {
          this.$message.error(resp.data.message)
        }
      })
    },
    uploadImgSuccess(resp) {
      this.activity.previewImg = resp.data;
      console.log(this.activity.previewImg);
    },
    uploadHtmlSuccess(resp) {
      this.activity.activityPage = resp.data;
    },
    addNewCoupon() {
      this.newCoupon.activityId = this.activity.activityId;
      if(!this.newCoupon.applyMulti) {
        this.newCoupon.applyMulti = false;
      }
      if(this.isUpdate) {
        this.axios.post('/coupon/add',this.newCoupon).then(resp=>{
          this.$msg(resp.data.success,resp.data.message);
          if(resp.data.success) {
            this.closeCouponDialog();
            this.coupons.push(this.newCoupon)
          }
        })
      } else {
        this.coupons.push(this.newCoupon)
        this.closeCouponDialog();
      }
    },
    closeCouponDialog() {
      this.couponDialog = false;
      this.unlimitUser = this.unlimitCount = false;      
    },
    onSubmit() {
      var path = this.isUpdate ? '/activity/update' : '/activity/add'
      this.axios.post(path,this.activity)
      .then(resp=>{
        this.$msg(resp.data.success,resp.data.message);
        if(!this.isUpdate) {
          this.addBatchCoupon(resp.data.data);
          this.$router.push('/activity?aid='+resp.data.data);
        }
      })
    },
    addBatchCoupon(aid) {
      this.coupons.forEach(i=>i.activityId = aid);
      this.axios.post('/coupon/add/batch',this.coupons).then(resp=>{
        if(!resp.data.success) {
          this.$msg(resp.data.success,resp.data.message);
        }
      })
    },
    unlimit(option) {
      if(option == 'count') {
        this.newCoupon.count = -1;
      } else if(option == 'user') {
        this.newCoupon.userLimit = -1;
      }
    },
    removeCoupon(cid,index) {
      if(this.isUpdate) {
        this.axios.get("/coupon/delete/"+cid).then(resp=>{
          this.$msg(resp.data.success,resp.data.message);
        })
      }
      this.coupons.splice(index,1);
    }
  }
}
</script>
<style lang="scss">
.activity-edit {
  .avatar-uploader {
    width: 350px;
    height: 200px;
      .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        line-height: 200px;
        text-align: center;
      }
      .avatar {
        width: 350px;
        height: 200px;
      }
      .el-upload {
        width: 350px;
        height: 200px;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
      }
  }
  margin: 50px auto;
  width: 40%;
  .el-form-item {
    margin-bottom: 45px;
  }
  .w-50p {
    width: 50%;
  }
  .w-100p {
    width: 100%;
  }
  .coupon-box {
    font-family: inherit;
    padding: 10px;
    border: 3px dashed #c0ccda;
    border-radius: 6px;
    width: 90%;
    height: fit-content;
    margin: 10px auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    align-content: center;
  }
}
</style>