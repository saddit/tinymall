<template>
  <div class="product-panel">
    <div class="status-select">
      <el-radio-group v-model="dto.productStatus" size="normal" @change="getProducts()">
        <el-radio-button :label="null"> 全部 </el-radio-button>
        <el-radio-button :label="0"> 已上架 </el-radio-button>
        <el-radio-button :label="1"> 未上架 </el-radio-button>
        <el-radio-button :label="2"> 已失效 </el-radio-button>
      </el-radio-group>
      <el-input @change="getProducts(page.pageNum)" v-model="dto.keyword" placeholder="关键字搜索" size="normal" clearable prefix-icon="el-icon-search"></el-input>
    </div>
    <div class="time-select">
      <el-date-picker class="time-picker" v-model="dateTimeRange" type="daterange" size="normal" @change="dateTimeRangeChange"
        range-separator="-" start-placeholder="创建时间" end-placeholder="创建时间">
      </el-date-picker>
      <el-select v-model="dto.categoryId" placeholder="商品分类" clearable @change="getProducts()">
        <el-option v-for="item in categories"
          :key="item.categoryId"
          :label="item.categoryDesc"
          :value="item.categoryId">
        </el-option>
      </el-select>
      
    </div>
    <div class="p-list">
      <el-table :data="products" border stripe fit>
        <el-table-column label="商品图" style="padding: 15px">
          <template #default="scope">
            <el-image :src="scope.row.previewImg" fit="fill" :lazy="true"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="商品分类">
          <template #default="scope"> 
            {{getCategroyName(scope.row.categoryId)}}
          </template>
        </el-table-column>
        <el-table-column
          v-for="col in columns"
          :prop="col.prop"
          :key="col.prop"
          :label="col.label"
        >
        </el-table-column>
        <el-table-column label="定价/售价">
          <template #default="scope">
            {{scope.row.salePrice}}/{{scope.row.originalPrice}}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button-group>
              <el-button size="default" @click="goProductPage(scope.row.productId)">修改</el-button>
              <el-button v-if="scope.row.productStatus == 0" size="default" @click="unDeploy(scope.row.productId)">下架</el-button>
              <el-button v-if="scope.row.productStatus != 0" size="default" @click="deploy(scope.row.productId)">发布</el-button>
              <el-button v-if="scope.row.productStatus != 0" size="default" @click="deleteProd(scope.row.productId)">删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-pagination
      @current-change="getProducts"
      :current-page="page.pageNum"
      :page-sizes="[15,30,40]"
      :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.totalNum" background
      :pager-count="7">
    </el-pagination>
    
  </div>
</template>
<script>
export default {
  data() {
    return {
      dateTimeRange: [],
      dto: {
        categoryId: null,
        productStatus: null,
      },
      page: {
        pageNum: 1,
        pageSize: 15
      },
      categories: [],
      categoryMap: {},
      products: [],
      columns: [
        {
          prop: 'productName',
          label: "商品名",
        },
        {
          prop: 'createTime',
          label: "创建时间",
        },
        {
          prop: 'deployTime',
          label: "发布时间",
        },
        {
          prop: 'stock',
          label: "库存",
        },
      ],
    };
  },
  mounted() {
    this.getCategroies();
    this.getProducts();
  },
  methods: {
    dateTimeRangeChange(newDateRange) {
      if(newDateRange) {
        this.dto.createTimeStart = newDateRange[0];
        this.dto.createTimeEnd = newDateRange[1];
      } else {
        this.dto.createTimeStart = this.dto.createTimeEnd = null;
      }
      this.getProducts();
    },
    getCategroyName(id) {
      return this.categoryMap[id];
    },
    goProductPage(productId) {
      this.$router.push('/product?pid='+productId);
    },
    getCategroies() {
      this.axios.get('/category/all')
        .then(resp => {
          if(resp.data.success) {
            this.categories = resp.data.data;
            this.categories.unshift({
              categoryId: null,
              categoryDesc: '全部'
            })
            resp.data.data.forEach(i => {
              this.categoryMap[i.categoryId] = i.categoryDesc;
            });
          }
        })
    },
    deleteProd(pid) {
      this.axios.get('/product/delete/'+pid).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        if(resp.data.success) {
          this.getProducts();
        }
      })
    },
    unDeploy(pid) {
      this.axios.get('/product/status/undeploy/'+pid).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        if(resp.data.success) {
          this.getProducts();
        }
      })
    },
    deploy(pid) {
      this.axios.get('/product/status/deploy/'+pid).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        if(resp.data.success) {
          this.getProducts();
        }
      })
    },
    getProducts(num) {
      if(num) {
        this.page.pageNum = num;
      }
      this.axios.post('/product/query/page/shop',{
        dto: this.dto,
        page: this.page
      }).then(resp=>{
        if(resp.data.success) {
          this.products = resp.data.data.list;
          this.page.totalNum = resp.data.data.totalNum;
        }
      })
    }
  }
};
</script>
<style lang="scss">
.product-panel {
  width: 80%;
  margin: 3% auto;
  .p-list {
    margin: 30px 0;
  }
  .time-select {
    margin: 20px 0;
    text-align: left;
    .time-picker {
      width: 30%;
      margin-right: 50%;
    }
    .el-select {
      width: 15%;
    }
  }
  .status-select {
    text-align: left;
    margin: 10px 0px 0;
    display: flex;
    justify-content: space-between;
    .el-input {
      width: 50%;
    }
  }
}
</style>