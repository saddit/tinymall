<template>
  <div class="order-content">
    <div class="header">
      <el-row :gutter="20" type="flex" justify="start" align="middle">
        <el-col :span="2" :offset="0">
          <el-image
            style="width: 50px; height: 50px"
            v-if="orderVo.shopIcon"
            :src="orderVo.shopIcon"
            fit="fill"
            :lazy="true"
          ></el-image>
        </el-col>
        <el-col :span="5" :offset="0">
          <div class="shopName">{{ orderVo.shopName }}</div>
        </el-col>
        <el-col :span="6" :offset="0"></el-col>
        <el-col :span="7" :offset="0">
            <count-down-view 
            v-if="!isSettle && orderVo.ordersStatus >=0 && orderVo.ordersStatus < 4"
            :start-date="getStartDate()"
            :interval-day="getInterval()"
            :text="getHelpText()"
            />
        </el-col>
        <el-col :span="4" :offset="0">
          <div class="status-label" v-if="!isSettle">
            {{ getOrderStatus() }}
          </div>
        </el-col>
      </el-row>
    </div>
    <hr
      style="filter: alpha(opacity=100, finishopacity=0, style=3)"
      size="1"
      color="#EBEEF5"
    />
    <div
      class="order-detail"
      v-for="(detail, i) in orderVo.items"
      :key="i"
    >
      <el-row :gutter="20">
        <el-col :span="5" :offset="0">
          <el-image :src="detail.productImg" fit="fill" :lazy="true"></el-image>
        </el-col>
        <el-col :span="14" :offset="0">
          <div class="detail-title">{{ detail.productName }}</div>
          <div class="detail-type">{{ detail.productType }}</div>
        </el-col>
        <el-col :span="5" :offset="0">
          <div class="detail-price">￥{{ detail.productPrice }}</div>
          <div class="detail-size">x{{ detail.productSize }}</div>
        </el-col>
      </el-row>
    </div>
    <div v-if="isSettle" class="select-coupon">
      <el-row :gutter="20">
        <el-col :span="9" :offset="0"></el-col>
        <el-col :span="3" :offset="0">
          <div style="height: 100%; width: 100%; padding: 10px 0">店铺优惠</div>
        </el-col>
        <el-col :span="6" :offset="0">
          <el-select-v2
            size="small"
            v-model="couponId"
            :options="couponOption"
            @visible-change="getCoupon"
            @change="onCouponChange"
            no-data-text="你还没有优惠券，快去领取吧"
          />
        </el-col>
        <el-col :span="6" :offset="0"></el-col>
      </el-row>
    </div>
    <el-collapse class="detail-panel" v-if="!isSettle" v-model="showDetail" :accordion="false" @change="handleDetailVisable">
      <el-collapse-item title="详情" :name="1">
        <el-descriptions size="small">
          <el-descriptions-item label="订单号">{{orderVo.ordersId}}</el-descriptions-item>
          <el-descriptions-item label="交易号">{{orderVo.payId}}</el-descriptions-item>
          <el-descriptions-item label="运单号">{{orderVo.expressId}}</el-descriptions-item>
          <el-descriptions-item label="快递商">{{orderVo.expressName}}</el-descriptions-item>
          <el-descriptions-item label="支付类型">{{orderVo.payType}}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{orderVo.createTime}}</el-descriptions-item>
          <el-descriptions-item label="发货时间">{{orderVo.expressTime}}</el-descriptions-item>
          <el-descriptions-item label="付款时间">{{orderVo.payTime}}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{orderVo.finishTime}}</el-descriptions-item>
          <el-descriptions-item label="收货地址">{{address}}</el-descriptions-item>
          <el-descriptions-item label="发货地址">{{orderVo.expressAddress}}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
    <div class="footer">
      <el-row :gutter="20">
        <el-col :span="6" :offset="0"></el-col>
        <el-col :span="6" :offset="0">
          总价：<span class="price">￥{{ orderVo.totalPrice }}</span>
        </el-col>
        <el-col :span="6" :offset="0">
          优惠：<span class="price">￥{{ getDiscount() }}</span>
        </el-col>
        <el-col :span="6" :offset="0">
          实付款：<span class="price">￥{{ isSettle ? discountPrice : orderVo.discountPrice}}</span>
        </el-col>
      </el-row>
    </div>
    <div v-if="!isSettle">
      <div v-if="orderVo.ordersStatus == 0" class="manage-btn-content">
        <el-button type="text" size="default" @click="optOrder('cancel')">取消订单</el-button>
        <el-button type="primary" size="default" @click="payOrder">付款</el-button>
      </div>
      <div v-else-if="orderVo.ordersStatus == 1" class="manage-btn-content">
        <el-button type="primary" size="default" @click="optOrder('refund')">退款</el-button>
      </div>
      <div v-else-if="orderVo.ordersStatus == 2" class="manage-btn-content">
        <el-button type="primary" size="default" @click="optOrder('receive')">确认收货</el-button>
      </div>
      <div v-else-if="orderVo.ordersStatus == 3"></div>
      <div v-else class="manage-btn-content">
        <el-button size="default" @click="optOrder('remove')">删除订单</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import CountDownView from './CountDownView.vue'
const orderStatusList = [
  {label:"待付款",prop:'createTime', interval: 7, help: '自动取消'},
  {label:"待发货",prop:'payTime', interval: 14, help: '自动退款'},
  {label:"待收货",prop:'expressTime', interval: 7, help: '自动收货'},
  {label:"待退款",prop:'refundTime', interval: 7, help: '自动退款'},
  {label:"已退款"},
  {label:"已完成"},
  {label:"已失效"},
];
export default {
  components: {CountDownView},
  data() {
    return {
      couponOption: [
        {
          value: null,
          label: '不使用优惠券'
        }
      ],
      totalDiscount: 0,
      discountPrice: this.orderVo.totalPrice,
      couponId: '',
      address: '',
      showDetail: [],
    };
  },
  props: {
    orderVo: () => {},
    isSettle: () => false,
  },
  emits: ["select-coupon", "price-change", "open-detail", 'flush'],
  methods: {
    getOrderStatus() {
      return orderStatusList[this.orderVo.ordersStatus || 0].label;
    },
    getInterval() {
      return orderStatusList[this.orderVo.ordersStatus || 0].interval;
    },
    getHelpText() {
      return orderStatusList[this.orderVo.ordersStatus || 0].help;
    },
    getStartDate() {
      var p = orderStatusList[this.orderVo.ordersStatus || 0].prop;
      return new Date(this.orderVo[p]);
    },
    onCouponChange(val) {
      if(val) {
        var coupon = this.couponOption.find((i) => i.value == val).data;
        var times = coupon.applyMulti
          ? Math.floor(this.orderVo.totalPrice / coupon.couponStep)
          : 1;
        if(coupon.couponType == 0) {
          this.totalDiscount = times * coupon.couponValue;
        } else {
          this.totalDiscount = (1 - Math.pow(coupon.couponValue * 0.01, times)) * this.orderVo.totalPrice;
        }
        this.totalDiscount = Math.round(this.totalDiscount * 100) / 100;
      } else {
        this.totalDiscount = 0;
      }
      var oldDisP = this.discountPrice;
      this.discountPrice = this.orderVo.totalPrice - this.totalDiscount;
      this.discountPrice = Math.round(this.discountPrice * 100) / 100;
      this.$emit("select-coupon", this.couponId);
      this.$emit("price-change", oldDisP, this.discountPrice);
    },
    getDiscount() {
      if (this.isSettle) {
        return this.totalDiscount;
      } else {
        return Math.floor((this.orderVo.totalPrice - this.orderVo.discountPrice)*100)/100;
      }
    },
    getCouponText(c) {
      if(c.couponType == 0) {
        if(c.couponStep == 0) {
          return '立减'+ c.couponValue;
        } else {
          return '满'+c.couponStep+'减'+c.couponValue;
        }
      } else {
        if(c.couponStep == 0) {
          return c.couponValue + '折券';
        } else {
          return '满'+c.couponStep+'打'+c.couponValue+'折';
        }
      }
    },
    getCoupon(visable) {
      if (visable && this.couponOption.length == 1) {
        this.axios.post('/coupon/query/user',{
          shopId: this.orderVo.shopId
        }).then(resp=>{
          if(resp.data.success) {
            resp.data.data.forEach((c) => {
              this.couponOption.push({
                value: c.couponId,
                label: this.getCouponText(c),
                data: c,
              });
            });
          }
        });
      }
    },
    handleDetailVisable(val) {
      if(val.length > 0 && !this.address) {
        this.axios.get('/address/get/'+this.orderVo.addressId).then(resp=>{
          this.address = resp.data.data.province + resp.data.data.city + resp.data.data.country + resp.data.data.street;
        })
      }
    },
    optOrder(option) {
      this.axios.get("/orders/opt/"+option+"/"+this.orderVo.ordersId)
      .then(resp=>{
        this.$msg(resp.data.success,resp.data.message);
        if(resp.data.success) {
          this.$emit('flush');
        }
      })
    },
    payOrder() {
      this.axios.get('/orders/pay/'+this.orderVo.ordersId)
      .then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        if(resp.data.success) {
          this.$emit('flush');
        }
      })
    }
  },
};
</script>
<style lang="scss">
.order-content {
  box-shadow: 1px 1px 3px #d9d9d9;
  border-radius: 5px;
  padding: 20px;
  margin-top: 25px;
  .detail-panel {
    width: 100%;
    padding-left: 15px;
    margin: 15px auto;
  }
  .header {
    .shopName {
      font-size: 18px;
    }
    .status-label {
      color: #ff0000;
    }
  }
  .manage-btn-content {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
    .el-button {
      margin: 0 10px;
    }
  }
  .select-coupon {
    margin-bottom: 15px;
  }
  .order-detail {
    margin-top: 3%;
    margin-bottom: 3%;
    .detail-title {
      margin: 25px;
      font-size: 18px;
      text-align: left;
    }
    .detail-type {
      margin-top: 20px;
      margin-left: 25px;
      font-size: 15px;
      text-align: left;
      color: gray;
    }
    .detail-price {
      color: #ff4400;
      font-weight: bold;
      font-size: 18px;
      margin: 25px auto;
    }
    .detail-size {
      margin-top: 20px;
      font-size: 15px;
      text-align: center;
      color: gray;
    }
  }
  .el-select-v2__placeholder {
    left: 0.5px;
    font-size: 13px;
  }
  .price {
    color: #ff4400;
    font-weight: bold;
  }
  .el-collapse-item__header {
    border-bottom: none;
    border-top: none;
  }
}
</style>