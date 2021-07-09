<template>
  <div class="coupon">
    <el-row :gutter="20" type="flex" align="middle" justify="start">
      <el-col :span="12" :offset="0">
        <div class="coupon-red price">{{getCounponValue}}</div>
      </el-col>
      <el-col :span="12" :offset="0">
        <div class="coupon-red detail">{{getCouponText}}</div>
        <el-button class="coupon-btn" type="primary" size="mini" @click="$emit('btn-click',id)">{{btnLabel}}</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  emits:['btn-click'],
  props: {
    coupon: ()=>{},
    btnLabel: ()=> '立即领取'
  },
  computed: {
    getCounponValue() {
      if(this.coupon.couponType == 0) {
        return "￥" + this.coupon.couponValue;
      } else {
        return this.coupon.couponValue + "%";
      }
    },
    getCouponText() {
      if(this.coupon.couponType == 0) {
        if(this.coupon.couponStep == 0) {
          return '立减'+ this.coupon.couponValue;
        } else {
          return '满'+this.coupon.couponStep+'减'+this.coupon.couponValue;
        }
      } else {
        if(this.coupon.couponStep == 0) {
          return this.coupon.couponValue + '折券';
        } else {
          return '满'+this.coupon.couponStep+'打'+this.coupon.couponValue+'折';
        }
      }
    }
  }
}
</script>
<style lang="scss">
.coupon-red {
  color: #E8220E;
}
.coupon {
  width: fit-content;
  height: fit-content;
  margin: 10px;
  padding: 3px 10px;
  box-shadow: 1px 1px 3px #d9d9d9;
  border-right: 1px solid #E8220E;
  border-bottom: 1px solid #E8220E;
  border-radius: 8px;
}
.coupon-btn {
  margin-top: 5px;
  font-size: 12px;
  width: 100%;
  padding: 3px;
  margin-left: 3px;
  margin-right: 3px;
  background-color: white;
  border: 1px solid #E8220E;
  border-radius: 8px;
  color: #E8220E;
}
.price {
  font-size: 25px;
  text-align: center;
  white-space: nowrap;
}
.detail {
  font-size: 12px;
  width: fit-content;
  text-align: center;
  white-space: nowrap;
  margin-left: 1px;
  margin-right: 1px;
}
</style>