<template>
  <div class="order-detail-panel">
    <div class="detail-desc">
      <div class="l-title">相关信息</div>
      <el-descriptions border :column="3">
        <el-descriptions-item label="订单号">{{order.ordersId}}</el-descriptions-item>
        <el-descriptions-item label="交易号">{{order.payId}}</el-descriptions-item>
        <el-descriptions-item label="运单号">{{order.expressId}}</el-descriptions-item>
        <el-descriptions-item label="快递商">{{order.expressName}}</el-descriptions-item>
        <el-descriptions-item label="支付类型">{{order.payType}}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{order.createTime}}</el-descriptions-item>
        <el-descriptions-item label="发货时间">{{order.expressTime}}</el-descriptions-item>
        <el-descriptions-item label="付款时间">{{order.payTime}}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{order.finishTime}}</el-descriptions-item>
        <el-descriptions-item label="收货地址">{{address}}</el-descriptions-item>
        <el-descriptions-item label="发货地址">{{order.expressAddress}}</el-descriptions-item>
      </el-descriptions>
    </div>
    <div class="detail-table">
      <div class="l-title">详单信息</div>
      <el-table :data="order.items" border stripe fit>
        <el-table-column label="预览图">
          <template #default="scope">
          <el-image :src="scope.row.productImg" fit="fit" :lazy="true"></el-image>
          </template>
        </el-table-column>
        <el-table-column v-for="col in columns"
          :prop="col.prop"
          :key="col.prop"
          :label="col.label">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      address: '',
      order:  {},
      columns: [
        {
          prop: 'detailId',
          label: '详单号'
        },
        {
          prop: 'productName',
          label: '商品名称'
        },
        {
          prop: 'productType',
          label: '款式'
        },
        {
          prop: 'productSize',
          label: '购买数量',
        },
        {
          prop: 'productPrice',
          label: '成交价'
        }
      ]
    }
  },
  mounted() {
    var oid = this.$route.query.oid;
    if(oid) {
      this.getOrder(oid);
    }
  },
  methods: {
    getOrder(oid) {
      this.axios.get("/orders/query/"+oid)
      .then(resp=>{
        if(resp.data.success) {
          this.order = resp.data.data;
          this.getAddress(this.order.addressId);
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    getAddress(aid) {
      this.axios.get('/address/shop/get/'+aid).then(resp=>{
        var a = resp.data.data;
        this.address = a.province + a.city + a.country + a.street;
      })
    }
  }
}
</script>
<style lang="scss">
.order-detail-panel {
  width: 80%;
  margin: 50px auto;
  .detail-desc {
    margin-bottom: 50px;
  }
  .l-title {
    font-weight: bold;
    text-align: left;
    margin: 25px;
  }
}
</style>