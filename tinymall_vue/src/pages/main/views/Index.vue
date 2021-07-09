<template>
<div style="width: 70%; margin: 0 auto;">
  <div>
    <el-carousel trigger="hover" height="470px" :autoplay="true" :interval="3000" :loop="true" 
    indicator-position="outside" ref="carousel">
      <el-carousel-item  v-for="item,i in hotActivites" :key="i">
        <el-image :src="item.previewImg" fit="fill" :lazy="false"></el-image>
      </el-carousel-item>
    </el-carousel>
  </div>
  <div>
    <h2 style="text-align: left;">热门推荐</h2>
    <el-scrollbar height="fit-content">
      <div class="flex-content">
      <template v-for="product in hotProducts" :key="product.productId">
        <product-view
          class="item"
          :id="product.productId"
          :title="product.productName"
          :img-src="product.previewImg"
          :sale-price="product.salePrice"
          :origin-price="product.originalPrice"
          :shop-name="product.shopName"          
          @click="$router.push('/product?pid='+product.productId)"
        />
      </template>
      </div>
    </el-scrollbar>
  </div>
  <div>
    <h2 style="text-align: left;">猜你喜欢</h2>
    <el-row :gutter="40" type="flex" justify="space-between">
      <el-col :span="6" :offset="0" v-for="product in recommendProducts" :key="product.productId">
        <product-view
          style="margin-top: 10px; margin-left: 10px"
          :id="product.productId"
          :title="product.productName"
          :img-src="product.previewImg"
          :sale-price="product.salePrice"
          :origin-price="product.originalPrice"
          :shop-name="product.shopName"
          @click="$router.push('/product?pid='+product.productId)"
        />
      </el-col>
    </el-row>
  </div>
</div>
</template>

<script>
import ProductView from '../components/ProductView.vue'
export default {
  name: 'index',
  data() {
    return {
      hotActivites: [],
      hotProducts: [],
      recommendProducts: []
    }
  },
  components: {
    ProductView
  },
  mounted() {
    this.getHotProd();
    this.getHotActivities();
    this.getRecommend();
  },
  methods: {
    getHotProd() {
      this.axios.get('/product/query/hot/').then(resp=>{
        if(resp.data.success) {
          this.hotProducts = resp.data.data;
          this.$refs.carousel.setActiveItem(0);
        }
      })
    },
    getRecommend() {
      this.axios.get('/product/query/recommend').then(resp=>{
        if(resp.data.success) {
          this.recommendProducts = resp.data.data;
        }
      })
    },
    getHotActivities() {
      this.axios.get('/activity/query/hot/'+4).then(resp=>{
        if(resp.data.success) {
          this.hotActivites = resp.data.data;
        }
      })
    }
  }
}
</script>

<style>
.el-scrollbar__view .flex-content .item {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    width: fit-content;
    height: fit-content;
    margin: 10px;
    text-align: center;
    border-radius: 4px;
    background: #fef0f0;
    color: #f56c6c;
}
.el-scrollbar__view .flex-content {
    display: flex;
}
</style>
