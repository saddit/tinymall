<template>
  <div class="coupon">
    <el-row :gutter="20">
      <el-col :span="12" :offset="0">
        <div class="coupon-red price">￥{{disValue}}</div>
      </el-col>
      <el-col :span="12" :offset="0">
        <div class="coupon-red detail">满{{disStep}}减{{disValue}}</div>
        <el-button class="coupon-btn" type="primary" size="mini" @click="$emit('click',id)">{{btnLabel}}</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  props: {
    disValue: {
      type: String
    },
    disStep: {
      type: String
    },
    id: {
      type: String
    },
    btnLabel: ()=> '立即领取'
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
  margin: 5px;
  padding: 10px;
  /* background-color: #ffffff; */
  background-image: url(https://img11.360buyimg.com/imagetools/jfs/t1/87420/23/17219/1084/5e82f7acE6c4508fa/1b3f16e3a0d1cd6f.png);
  background-size: 100% 100%;
  /* display: inline-block; */
  border-radius: 8px;
  /* filter: url(#grayscale); */
}
.coupon-btn {
  margin-top: 5px;
  font-size: 12px;
  width: 100%;
  padding: 3px;
  background-color: white;
  border: 1px solid #E8220E;
  border-radius: 8px;
  color: #E8220E;
}
.price {
  font-size: 30px;
  padding-top: 5px;
  text-align: center;
  white-space: nowrap;
}
.detail {
  font-size: 12px;
  text-align: center;
  white-space: nowrap;
}
</style>