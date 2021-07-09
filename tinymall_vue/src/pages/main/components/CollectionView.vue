<template>
  <div class="coll-content">
    <el-row :gutter="20">
      <el-col :span="7" :offset="0">
        <el-image class="view-img" :src="coll.productImg" fit="fill" :lazy="true"></el-image>
      </el-col>
      <el-col :span="16" :offset="1">
        <div class="title">
          {{coll.productName}}
        </div>
        <div class="price">
          ￥{{coll.productPrice}}
        </div>
        <div class="footer">
          <div class="shop-name">{{coll.shopName}}</div>
          <el-button size="default" @click="addToCar(coll.productId)">加入购物车</el-button>
          <el-button size="default" @click="$emit('remove',coll.collId)">移除</el-button>
        </div>
      </el-col>
    </el-row>
    
  </div>
</template>
<script>
export default {
  props: {
    coll: ()=> {}
  },
  emits: ['remove'],
  methods: {
    addToCar(pid) {
      this.axios.post('/shopcar/add/',{
        productId: pid,
      }).then(resp=>{
        if(resp.data.success) {
          this.$message.success(resp.data.message);
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
  }
}
</script>
<style lang="scss">
.coll-content {
  box-shadow: 1px 1px 3px #d9d9d9;
  border-radius: 5px;
  padding: 20px;
  margin-top: 25px;
  .title {
    text-align: left;
    font-size: 18px;
    height: 50%;
    width: 90%;
  }
  .price {
    color: #ff4477;
    text-align: right;
    width: 90%;
    height: 25%;
    font-size: 20px;
    font-weight: bold;
  }
  .view-img {
    width: 200px;
    height: 200px;
  }
  .footer {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    .shop-name {
      color: gray;
      font-size: 14px;
      width: 50%;
      text-align: left;
      cursor: pointer;
      &:hover {
        opacity: 80%;
      }
    }
  }
}
</style>