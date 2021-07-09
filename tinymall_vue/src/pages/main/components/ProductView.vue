<template>
  <div class="product-view"> 
    <el-card class="product-card" shadow="never" :body-style="{ padding: '5px' }" @click="$emit('click', id)">
      <template #header>
        <el-image :src="imgSrc" fit="fill" :lazy="true"></el-image>
      </template>
      <el-row class="product-row" type="flex" justify="space-between" align="middle">
        <el-col v-if="title" :span="10" :offset="0"><p id="title">{{title.slice(0,38)}}</p></el-col>
        <el-col :span="2" :offset="0"></el-col>
        <el-col :span="6" :offset="0">
          <span v-if="salePrice" class="price-item" id="sale-price">¥{{salePrice}}</span>
        </el-col>
        <el-col :span="6" :offset="0">
          <span v-if="originPrice" class="price-item" id="original-price">¥{{originPrice}}</span>
        </el-col>
      </el-row>
      <div class="shop-name">{{shopName}}</div>
    </el-card>
  </div>
</template>
<script>
export default {
  props: {
    id: {
      type: String
    },
    imgSrc: {
      type: String
    },
    title: {
      default: ()=> null,
      type: String
    },
    salePrice: {
      default: ()=> null,      
      type: String
    },
    originPrice: {
      default: ()=> null,      
      type: String
    },
    shopName: ()=> '',
  },
  emits:['click']
}
</script>

<style lang="scss">
.product-view {
  .product-card {
    width: 300px;
    height: 350px;
    .el-image {
      height: 200px;
      width: 200px;
    }
  }
  .product-row {
    padding: 0;
    margin: 0;
  }
  #title {
    font-size: 10px;
    text-align: left;
  }
  #original-price {
    color: #FF4400;
    font-size: 12px;
    text-decoration:line-through;
  }
  #sale-price {
    color: #FF4400;
    font-size: 18px;
    font-weight: bold;
  }
  .price-item {
    display: inline;
    margin: 5px;
  }
  .shop-name {
    text-align: left;
    font-size: 12px;
    color: gray;
    text-decoration: underline;
    margin: 2px;
    height: fit-content;
  }
}
</style>