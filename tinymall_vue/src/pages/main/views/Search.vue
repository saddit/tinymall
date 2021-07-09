<template>
  <div class="search-content">
    <div class="cate-selector">
      <p style="text-align: left;" class="selector-title">所有分类</p>
      <el-row :gutter="20" type="flex" justify="start">
        <el-radio-group v-model="dto.categoryId" @change="getProducts()">
          <el-col v-for="item in categories" :key="item.categoryId" :span="8" :offset="0">
            <el-radio :label="item.categoryId">
              {{item.categoryDesc}}
            </el-radio>
          </el-col>
        </el-radio-group>
      </el-row>
    </div>
    <div class="tool-bar">
      <el-row :gutter="10">
        <el-col :span="7" :offset="0">
          <el-radio-group v-model="page.orderBy" size="normal" @change="getProducts()">
            <el-radio-button :label="null">默认</el-radio-button>
            <el-radio-button label="pv">按销量排序</el-radio-button>
            <el-radio-button label="sale_price">按价格排序</el-radio-button>
          </el-radio-group>
        </el-col>
        <el-col :span="1.5" :offset="0">
          <div class="price-text">价格</div>
        </el-col>
        <el-col :span="3">
          <el-input v-model="dto.minPrice" @change="getProducts()"></el-input>
        </el-col>
        <el-col :span="1" :offset="0">
          <div class="price-text">—</div>
        </el-col>
        <el-col :span="3">
          <el-input v-model="dto.maxPrice" @change="getProducts()"></el-input>
        </el-col>
        <el-col :span="5" :offset="3">
          <el-pagination
            @current-change="getProducts"
            :current-page="page.pageNum"
            :page-size="page.pageSize"
            :page-count="page.totalPage"
            layout="prev, pager, next"
            :pager-count="1">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
    <div class="product-content" v-loading="loading">
      <el-row :gutter="20" type="flex" justify="start">
        <el-col v-for="item in products" :key="item.productId" :span="7" :offset="0">
          <product-view 
            style="margin-top: 10px; width: 250px; height: 370px;"
            :id="item.productId"
            :title="item.productName"
            :sale-price="item.salePrice"
            :origin-price="item.originalPrice"
            :img-src="item.previewImg"
            :shop-name="item.shopName"
            @click="$router.push('/product?pid='+item.productId)"
          />
        </el-col>           
      </el-row>
    </div>
    <el-pagination
        hide-on-single-page
        @current-change="getProducts"
        :current-page="page.pageNum"
        :page-size="page.pageSize"
        layout="prev, pager, next, jumper"
        :page-count="page.totalPage" background
        :pager-count="7">
      </el-pagination>
  </div>
</template>
<script>
import ProductView from '../components/ProductView.vue'
export default {
  data() {
    return {
      products:[],
      categories: [],
      loading: true,
      page: {
        pageNum: 1,
        pageSize: 30,
        orderBy: null,
      },
      dto: {
        categoryId: null,
        minPrice: null,
        maxPrice: null,
        keyword: null,
        activityId: null,
      }
    }
  },
  components: {ProductView},
  mounted() {
    this.getCategories();
    if(this.$route.query.keyword) {
      this.dto.keyword = this.$route.query.keyword;
    }
    if(this.$route.query.aid) {
      this.dto.activityId = this.$route.query.aid;
    }
    if(this.$route.query.cid) {
      this.dto.categoryId = this.$route.query.cid;
    }
    this.getProducts();
  },
  methods: {
    getProducts(num) {
      this.loading = true;
      if(num) {
        this.page.pageNum = num;
      }
      this.axios.post('/product/query/page',{
        dto: this.dto,
        page: this.page,
      }).then(resp=>{
        if(resp.data.success) {
          this.products = resp.data.data.list;
          this.page = resp.data.data;
          this.page.list = null;
          if(!this.page.orderBy) {
            this.page.orderBy = null;
          }
          this.loading = false;
          this.dto.keyword = null;
        }
      })
    },
    getCategories() {
      this.axios.get('/category/all').then(resp=>{
        if(resp.data.success) {
          this.categories = resp.data.data;
          this.categories.unshift({
            categoryId: null,
            categoryDesc: '全部'
          })
        }
      })
    }
  }
}
</script>
<style lang="scss">
.search-content {
  width: 60%;
  padding: 40px;
  margin: 0 auto;
}
.tool-bar {
  margin-top: 40px;
  margin-bottom: 40px;
  .price-text {
    padding: 5px; 
    color: grey; 
    font-size: 14px; 
    margin: 5px auto;
  }
  .el-button-group {
    display: flex;
    justify-content: flex-start;
  }
  .el-pagination {
    margin: 5px auto;
  }
}
.cate-selector {
  .selector-title {
    color: gray;
  }
}
</style>