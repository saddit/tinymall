<template>
  <div class="orderlist-panel">
    <div class="tool-bar">
      <div class="status-select">
        <el-radio-group v-model="dto.ordersStatus" size="normal" @change="getOrders()">
          <el-radio-button :label="null">全部</el-radio-button>
          <el-radio-button :label="0">未付款</el-radio-button>
          <el-radio-button :label="1">未发货</el-radio-button>
          <el-radio-button :label="2">未收货</el-radio-button>
          <el-radio-button :label="3">待退款</el-radio-button>
        </el-radio-group>
      </div>
      <div class="date-select">
        <div class="date-time-label">日期时间</div>
        <el-date-picker
          v-model="dateTimeRange"
          type="datetimerange"
          :shortcuts="shortcuts"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"                      
          @change="dateRangeChange"
          >
        </el-date-picker>
      </div>
      <div class="search">
        <el-input @change="getOrders(page.pageNum)" prefix-icon="el-icon-search" v-model="dto.keyword" placeholder="搜索订单号/交易号" size="normal" clearable></el-input>
      </div>
    </div>
    <div class="order-table">
      <el-table :data="orders" border stripe fit>
        <el-table-column v-for="col in columns"
          :prop="col.prop"
          :key="col.prop"
          :label="col.label">
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button v-if="[4,5,6].indexOf(scope.row.ordersStatus) == -1" type="primary" size="default" @click="orderOpt(scope.row)">{{getOptLabel(scope.row.ordersStatus)}}</el-button>
            <el-button size="default" @click="goDetail(scope.row.ordersId)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
    </div>
    <el-pagination
      @current-change="getOrders"
      :current-page="page.pageNum"
      :page-sizes="[15,30,40]"
      :page-size="page.pageSize"
      layout="total,sizes, prev, pager, next, jumper"
      :total="page.totalNum" background
      :pager-count="7">
    </el-pagination> 
  </div>
  <el-dialog
    title="发货"
    v-model="expressDialog"
    width="30%">
    <span>
      <el-form :model="expressOrder" ref="form" :rules="rules" label-width="80px" :inline="false" size="normal">
        <el-form-item label="运单号">
          <el-input v-model="expressOrder.expressId" placeholder="物流单号"></el-input>
        </el-form-item>
        <el-form-item label="快递公司">
          <el-input v-model="expressOrder.expressName" placeholder="快递公司名称" size="normal"></el-input>
        </el-form-item>
        <el-form-item label="发货地址">
          <el-input v-model="expressOrder.expressAddress" size="normal"></el-input>
        </el-form-item>
      </el-form>
    </span>
    <template #footer>
      <el-button @click="expressDialog = false">取消</el-button>
      <el-button type="primary" @click="doExpressOrder">确认发货</el-button>
    </template>
  </el-dialog>
</template>
<script>
import Const from "@/js/const"
export default {
  data() {
    return {
      shortcuts: Const.defShortcuts,
      page:{
        pageNum:1,
        pageSize: 15
      },
      orders:[],
      columns:[
        {
          prop: 'ordersId',
          label: '订单号',
        },
        {
          prop: 'payId',
          label: '交易号',
        },
        {
          prop: 'receiveTime',
          label: '收货时间'
        },
        {
          prop: 'payTime',
          label: '付款时间',
        },
        {
          prop: 'createTime',
          label: '下单时间'
        },
        {
          prop: 'totalPrice',
          label: '实付款'
        }
      ],
      dto:{
        ordersStatus: null,
      },
      expressDialog: false,
      expressOrder: null,
    }
  },
  mounted() {
    this.getOrders();
  },
  methods: {
    dateRangeChange(newDateRange) {
      if(newDateRange) {
        this.dto.createTimeStart = newDateRange[0];
        this.dto.createTimeEnd = newDateRange[1];
      } else {
        this.dto.createTimeStart = this.dto.createTimeEnd = null;
      }
      this.getNotices();
    },
    orderOpt(order) {
      var option = this.getOption(order.ordersStatus);
      switch(option) {
        case 'notify-pay':
          var notice = {
            noticeTitle: '请及时付款',
            content: '订单号' + order.ordersId + '已经下单成功,请及时付款,24小时候将自动取消'
          }
          this.axios.post('/notice/add',{
            notice: notice,
            to: [order.userId]
          }).then(resp=>this.$msg(resp.data.success,resp.data.message));
          break;
        case 'express':
          this.expressOrder = order;
          this.expressDialog = true
          break;
        case 'notify-receive':
          notice = {
            noticeTitle: '请及时确认收货',
            content: '订单号' + order.ordersId + '已经发货,请及时确认收货，7天后将自动确认'
          }
          this.axios.post('/notice/add',{
            notice: notice,
            to: [order.userId]
          }).then(resp=>this.$msg(resp.data.success,resp.data.message));
          break;
        case 'refund':
          this.axios.get('/orders/refund/agree/'+order.ordersId).then(resp=>this.$msg(resp.data.success,resp.data.message));
          break;
      }
    },
    doExpressOrder() {
      this.axios.post('/orders/express',this.expressOrder)
      .then(resp=>{
        this.$message.info(resp.data.message);
        if(resp.data.success){
          this.expressDialog = false;
        }
      })
    },
    getOption(status) {
      switch(status) {
        case 0: return 'notify-pay';
        case 1: return 'express';
        case 2: return 'notify-receive';
        case 3: return 'refund';
      }
    },
    getOptLabel(status) {
      switch(status) {
        case 0: return '提醒';
        case 1: return '发货';
        case 2: return '提醒';
        case 3: return '退款';
      }
    },
    getOrders(num) {
      if(num) {
        this.page.pageNum = num;
      }
      this.axios.post('/orders/query/shop/page',{
        dto: this.dto,
        page: this.page
      }).then(resp=>{
        if(resp.data.success) {
          this.orders = resp.data.data.list;
          this.page.totalNum = resp.data.data.totalNum;
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    goDetail(oid) {
      this.$router.push("/order?oid=" + oid);
    }
  }
}
</script>
<style lang="scss">
.orderlist-panel {
  width:80%;
  margin: 25px auto;
  .tool-bar {
    margin-bottom: 50px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    .date-select {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      margin-right: 30px;
      .date-time-label {
        margin-right: 20px;
      }
    }
    .search {
      width: 25%;
    }
    .status-select {
      margin-right: 30px;
    }
  }
  .order-table {
    margin-bottom: 30px;
  }
}
</style>