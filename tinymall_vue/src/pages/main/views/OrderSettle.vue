<template>
<div
  v-loading="onPlaceOrder"
  element-loading-text="正在生成订单.."
  element-loading-spinner="el-icon-loading"
>
  <el-affix class="bar-affix" position="top">
    <div class="bottom-bar">
      <el-row :gutter="10" type="flex" justify="start" align="center">
        <el-col :span="18" :offset="0"></el-col>
        <el-col :span="0.5" :offset="0">
          <div style="font-size: 13px; color: grey; padding-top: 10px;">总价</div>
        </el-col>
        <el-col :span="2" :offset="0">
          <div style="color: #FF4400; font-weight: bold; height: 100%; padding-top: 5px;">￥{{paidPrice}}</div>
        </el-col>
        <el-col :span="1" :offset="0">
          <el-button type="primary" size="small" @click="settelOrder">确认</el-button>
        </el-col>
        <el-col :span="1">
          <el-button type="primary" size="small" @click="$router.push('/shopcar')">取消</el-button>
        </el-col>
        <el-col :span="1" :offset="0"></el-col>
      </el-row>
    </div>
  </el-affix>
  <address-view 
    v-if="defaultAddr"
    style="width: 30%; margin: 10px auto;"
    :isSelect="true"
    :address="defaultAddr"
    @select="selectAddress"
  />
  <div v-else>
    <el-button type="text" size="default" @click="$router.push('/address')">您还没有设定地址，请点击前往设置。</el-button>    
  </div>
  <div style="margin: 0 auto; width: 40%;">
    <order-view 
    v-for="o in orders"
    :key="o.shopId"
    :isSettle="true"
    :orderVo="o"
    @select-coupon="(cid) => selectCoupon(o,cid)"
    @price-change="onPriceChange"
    />
  </div>
  <el-dialog
    title="选择地址"
    v-model="showAllAddr"
    width="50%">
    <span>
      <el-scrollbar v-if="allAddrs.length > 0" height="500px">
        <address-view 
          v-for="item,i in allAddrs"
          :key="i"
          style="width: 70%; margin: 10px auto;"
          :isSelect="true"
          :address="item"
          @select="changeAddr(item)"
        />
      </el-scrollbar>
      <el-empty v-else description="没有地址信息" />
    </span>
  </el-dialog>
</div>
</template>
<script>
import AddressView from '../components/AddressView.vue';
import OrderView from '../components/OrderView.vue';
export default {
  components: { OrderView, AddressView },
  data() {
    return {
      orders: [],
      paidPrice: 0,
      defaultAddr: null,
      allAddrs:[],
      showAllAddr: false,
      carIds: [],
      onPlaceOrder: false,
    }
  },
  methods: {
    getDefaultAddr() {
      //request
      this.axios.get('/address/get/default').then(resp=>{
        if(resp.data.success) {
          this.defaultAddr = resp.data.data;
          this.changeOrdersAddr(this.defaultAddr.addressId);
        }
      })
    },
    getAllAddresses() {
      this.axios.get('/address/get').then(resp=>{
        if(resp.data.success) {
          this.allAddrs = resp.data.data;
        }
      })
    },
    changeAddr(addr) {
      this.defaultAddr = addr; 
      this.showAllAddr=false;
      this.changeOrdersAddr(this.defaultAddr.addressId);
    },
    async changeOrdersAddr(addrId) {
      this.orders.forEach(i=>{
        i.addressId = addrId;
      });
    },
    settelOrder() {
      this.onPlaceOrder = true;
      this.axios.post('/orders/place',this.orders).then(resp=>{
        if(resp.data.success) {
          // 付款页面
          this.removeShopCar();
          this.$router.push({
            name: "orderlist",
            params: {
              status: 0,
            }
          })
        } else {
          this.$alert(resp.data.message);
        }
      }).finally(()=>{
        this.onPlaceOrder = false;
      })
    },
    removeShopCar() {
      this.axios.post('/shopcar/delete/batch',this.carIds);
    },
    onPriceChange(oldVal, newVal) {
      this.paidPrice += newVal-oldVal;
      this.paidPrice = Math.round(this.paidPrice * 100) / 100;
    },
    getOrdersFromCarItems(carItems) {
      var orderMap = new Map();
      carItems.forEach(item => {
        if(item.carId){
          this.carIds.push(item.carId);
        }
        var price = Number.parseFloat(item.productPrice) * Number.parseInt(item.productSize)
        if(!orderMap.has(item.shopId)) {
          orderMap.set(item.shopId,{
            shopId: item.shopId,
            shopName: item.shopName,
            shopIcon: item.shopIcon,
            addressId: '',
            couponId: null,
            totalPrice: price,
            items: [item]
          })
        } else {
          var order = orderMap.get(item.shopId)
          order.items.push(item)
          order.totalPrice += price;
          order.totalPrice = Math.round(order.totalPrice * 100) / 100;
          order.discountPrice = order.totalPrice;
        }
        this.paidPrice += price;
        this.paidPrice = Math.round(this.paidPrice * 100) / 100;
      });
      this.orders = [...orderMap.values()];
    },
    selectCoupon(order, couponId) {
      order.couponId = couponId;
    },
    selectAddress() {
      if(this.allAddrs.length == 0){
        this.getAllAddresses();
      }
      this.showAllAddr = true;
    }
  },
  mounted () {
    if(this.$route.params.data) {
      this.getOrdersFromCarItems(JSON.parse(this.$route.params.data))
      this.getDefaultAddr();
    }
  }
}
</script>
<style lang="">
  
</style>