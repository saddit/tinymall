<template>
  <div class="report-view">
    <el-space wrap>
      <el-card shadow="never">
        <div id="sale" style="width: 600px;height:400px;"></div>
      </el-card>
      <el-card shadow="never">
        <div style="margin-bottom: 15px;">
          <el-date-picker style="margin-right: 15px;" v-model="incomeDto.year" type="year" :clearable="false" placeholder="选择年份" @change="generateMonthIncome" />
          <el-dropdown trigger="click" type="text"  split-button  @command="onCommandChange">
            选择月份
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  command="pre">
                  前半年
                </el-dropdown-item>
                <el-dropdown-item
                  command="after">
                  后半年
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div id="month-income" style="width: 600px;height:400px;"></div>
      </el-card>
    </el-space>
  </div>
</template>
<script>
const colors1 = ['#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83', '#ca8622'];
const colors2 = ['#61a0a8','#c23531','#2f4554', '#d48265', '#91c7ae','#749f83', '#ca8622'];
export default {
  data() {
    return {
      incomeDto: {
        year: '2021',
        endMonth: 6,
        startMonth: 1,
      }
    }
  },
  components:{},
  created() {
    var date = new Date();
    this.incomeDto.year = date.getFullYear().toString();

    if(date.getMonth()+1 > 6) {
      this.onCommandChange('after');
    }
  },
  mounted() {
    this.generateSaleReport();
    this.generateMonthIncome();
  },
  methods: {
    onCommandChange(command) {
      if(command == 'pre') {
        this.incomeDto.startMonth = 1;
        this.incomeDto.endMonth = 6;
      } else if (command == 'after') {
        this.incomeDto.startMonth = 7;
        this.incomeDto.endMonth = 12;
      }
      this.generateMonthIncome();
    },
    async generateSaleReport() {
      var sale = this.$echarts.init(document.getElementById('sale'));
      var res = await this.axios.get("/category/shop/pv");
      if(!res.data.success) {
        this.$msg(false, '数据获取异常');
        return;
      }
      var option = {
        color: colors1,
        title: {
          text: '销量报表'
        },
        tooltip: {},
        legend: {
          data: ['销量']
        },
        xAxis: {
          data: []
        },
        yAxis: {},
        series: [{
          name: '销量',
          type: 'bar',
          data: [],
        }]
      }
      res.data.data.forEach(item=>{
        option.xAxis.data.push(item.categoryDesc);
        option.series[0].data.push(item.pv);
      })
      sale.setOption(option);
    },
    async generateMonthIncome() {
      var income = this.$echarts.init(document.getElementById('month-income'));
      var res = await this.axios.post("/orders/shop/income/month",this.incomeDto);
      if(!res.data.success) {
        this.$msg(false, '数据获取异常');
        return;
      }
      var option = {
        color: colors2,
        title: {
          text: '收入报表'
        },
        tooltip: {},
        legend: {
          data: ['收入']
        },
        xAxis: {
          data: []
        },
        yAxis: {},
        series: [{
          name: '收入',
          type: 'bar',
          data: [],
        }]
      }
      res.data.data.forEach(item=>{
        option.xAxis.data.push(item.month + '月');
        option.series[0].data.push(item.income);
      })
      income.setOption(option);
    }
  }
}
</script>
<style lang="scss">
.report-view {
  width: 80%;
  margin: 0 auto;
}
</style>