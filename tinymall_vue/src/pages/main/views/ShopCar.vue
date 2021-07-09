<template>
  <el-affix v-if="shopCars.length > 0" class="bar-affix" position="top">
    <div class="bottom-bar">
      <el-row :gutter="10" type="flex" justify="start" align="center">
        <el-col :span="19" :offset="0"></el-col>
        <el-col :span="1" :offset="0">
          <div style="font-size: 13px; color: grey; padding-top: 10px;">总价</div>
        </el-col>
        <el-col :span="1" :offset="0">
          <div style="color: #FF4400; font-weight: bold; height: 100%; padding-top: 5px;">￥{{totalPrice}}</div>
        </el-col>
        <el-col :span="2" :offset="0">
          <el-button type="primary" size="small" @click="goSettle">结算</el-button>
        </el-col>
        <el-col :span="1"></el-col>
      </el-row>
    </div>
  </el-affix>
  <div style="margin: 0 auto; width: 40%;">
    <div v-if="shopCars.length > 0">
      <shop-car-view
        v-for="(car, index) in shopCars" :key="index" 
        :car="car"
        @select-item="selectItem"
        @delete-item="removeItem"
      />
      <el-pagination
        hide-on-single-page
        @current-change="getShopCars"
        :current-page="page.pageNum"
        :page-size="page.pageSize"
        layout="prev, pager, next, jumper"
        :page-count="page.totalPage" background
        :pager-count="3">
      </el-pagination>
    </div>
    <div v-else>
      <el-empty description="购物车空空如也，快去加购吧"></el-empty>
    </div>
  </div>
</template>
<script>
import ShopCarView from '../components/ShopCarView.vue'
export default {
  data() {
    return {
      shopCars: [],
      selectedList:[],
      itemIndex: {},
      totalPrice: 0.00,
      page: {
        pageNum: 1,
        pageSize: 3
      },
    }
  },
  components: {ShopCarView},
  mounted() {
    this.getShopCars();
  },
  methods: {
    getShopCars(num) {
      if(num) {
        this.page.pageNum = num;
      }
      this.axios.post('/shopcar/get',this.page).then(resp=>{
        if(resp.data.success) {
          this.shopCars = resp.data.data.list;
          this.page = resp.data.data;
          this.page.list = null;
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    selectItem(checked,car,item) {
      if(checked) {
        item['shopName'] = car.shopName;
        item['shopIcon'] = car.shopIcon;
        this.selectedList.push(item);
        this.totalPrice += Number.parseFloat(item.productPrice) * Number.parseInt(item.productSize);
      } else {
        var idx = this.selectedList.findIndex(i=> i.productId == item.productId);
        this.selectedList.splice(idx,1);
        this.totalPrice -= Number.parseFloat(item.productPrice) * Number.parseInt(item.productSize);
      }
      this.totalPrice = Math.round(this.totalPrice * 100) / 100;
    },
    removeItem(car,item) {
      this.axios.get('/shopcar/delete/'+item.carId).then(resp=>{
        if(resp.data.success) {
          if(item.checked) {
            this.selectItem(false,car, item);
          }
          this.getShopCars();
        } else {
          this.$message.error(resp.data.message)
        }
      })
    },
    goSettle() {
      if(this.selectedList.length == 0) {
        this.$message.error("购物车为空无法结算");
      }
      this.$router.push({
        name: 'ordersettle',
        params: {
          data: JSON.stringify(this.selectedList)
        }
      })
    }
  }
}
</script>
<style lang="scss">
</style>