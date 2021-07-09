<template>
  <div class="main-content">
    <div class="region-content" v-for="cp in cateProduct" :key="cp.categoryId">
      <el-row>
        <el-col :span="18" :offset="0">
          <p style="text-align: left; font-size: 40px; margin: 10px 5px;">{{cp.categoryDesc}}</p>
        </el-col>
        <el-col :span="4" :offset="2">
          <el-button style="margin-top: 20px;" type="info" size="default" circle icon="el-icon-arrow-right" @click="goSearch(cp.categoryId)"></el-button>
        </el-col>
      </el-row> 
      <div v-if="cp.products.length == 0">
        <el-button type="text" size="default" disabled>暂时没有商品</el-button>
      </div>
      <el-row v-else :gutter="50" type="flex" justify="start">
        <el-col style="margin-top: 10px;" :span="8" v-for="product in cp.products" :key="product.productId">
          <product-view
          :id="product.productId"
          :img-src="product.previewImg"
          :title="product.productName"
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
import ProductView from '../components/ProductView.vue';
export default {
  data() {
    return {
      cateProduct: [],
    };
  },
  components: {ProductView},
  mounted() {
    this.getProduct();
  },
  methods: {
    getProduct() {
      this.axios.get('/category/product').then(resp=>{
        if(resp.data.success) {
          this.cateProduct = resp.data.data;
        }
      })
    },
    goSearch(cid) {
      this.$router.push('/search?cid='+cid)
    }
  },
};
</script>
<style lang="scss">
.main-content {
  padding: 50px;
}
.region-content {
  width: 60%;
  margin: 80px auto;
  border: 5px solid #ff4400;
  padding: 3%;
  border-radius: 25px;
}
</style>