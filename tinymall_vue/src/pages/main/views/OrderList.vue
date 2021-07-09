<template>
  <div class="order-toolbar">
    <el-radio-group v-model="statusLabel" @change="onSelectType">
      <el-radio-button label="全部"></el-radio-button>
      <el-radio-button label="未支付"></el-radio-button>
      <el-radio-button label="未发货"></el-radio-button>
      <el-radio-button label="未收货"></el-radio-button>
      <el-radio-button label="已完成"></el-radio-button>
    </el-radio-group>
    <el-input style="width: 25%;" v-model="dto.keyword" placeholder="输入关键字搜索" size="normal" clearable @change="getOrders(page.pageNum)" prefix-icon="el-icon-search"></el-input>
    <el-dropdown trigger="click" size="default" @command="onSelectTimeRange">
       <span class="el-dropdown-link">
        选择日期<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item :command="0">全部</el-dropdown-item>
          <el-dropdown-item :command="1">近一周内</el-dropdown-item>
          <el-dropdown-item :command="2">一个月内</el-dropdown-item>
          <el-dropdown-item :command="3">三个月内</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
  <div style="margin: 0 auto; width:40%;" v-if="orders.length > 0">
    <order-view
      v-for="o in orders"
      :key="o.ordersId"
      :orderVo="o"
      :isSettle="false"
      @flush="getOrders"
    />
  </div>
  <div v-else>
    <el-empty description="暂时没有订单" />
  </div>
  <el-pagination
    hide-on-single-page
    @current-change="getOrders"
    :current-page="page.pageNum"
    :page-size="page.pageSize"
    layout="prev, pager, next"
    background
    :page-count="page.totalPage"
    :pager-count="3">
  </el-pagination>
  
</template>
<script>
import OrderView from '../components/OrderView.vue'
import Api from '@/js/api'
const statusMap = {
  '全部': null,
  '未支付': 0,
  '未发货': 1,
  '未收货': 2,
  '已完成': 5
}
const statusMap2 = {
  0: '未支付',
  1: '未发货',
  2: '未收货',
  5: '已完成'
}
export default {
  components: { OrderView },
  data() {
    return {
      dto:{},
      page:{
        pageNum: 1,
        pageSize: 3,
        orderby: 'create_time',
        isDesc: true
      },
      statusLabel: "全部",
      orders:[]
    }
  },
  mounted() {
    if(this.$route.params.status) {
      this.dto.ordersStatus = this.$route.params.status;
      this.statusLabel = statusMap2[this.dto.ordersStatus];
    }
    this.getOrders(this.page.pageNum);
  },
  methods: {
    onSelectType(label) {
      this.dto.ordersStatus = statusMap[label]
      this.getOrders();
    },
    onSelectTimeRange(command) {
      this.dto.createTimeEnd = Api.getLocalDateTime();
      switch(command) {
        case 1:
          this.dto.createTimeStart = Api.getLocalDateTime({week: -1});
          break;
        case 2:
          this.dto.createTimeStart = Api.getLocalDateTime({month: -1});
          break;
        case 3:
          this.dto.createTimeStart = Api.getLocalDateTime({month: -3});
          break;
        case 0:
          this.dto.createTimeEnd = this.dto.createTimeStart = null;
          break;
      }
      this.getOrders();
    },
    getOrders(pageNum) {
      if(pageNum) {
        this.page.pageNum = pageNum;
      }
      this.axios.post('/orders/query/page',{
        dto: this.dto,
        page: this.page
      }).then(resp=>{
        if(resp.data.success) {
          this.orders = resp.data.data.list;
          this.page.totalPage = resp.data.data.totalPage;
        }
      })
    },
  }
}
</script>
<style lang="scss">
.order-toolbar {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 50%;
  margin: 0 auto;
  margin-bottom: 20px;
  .el-dropdown-link {
    cursor: pointer;
    color: #ff4400;
  }
}
</style>