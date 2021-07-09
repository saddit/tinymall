<template>
  <div class="activity-panel">
    <div class="tool-bar">
      <div class="status-select">
        <el-radio-group v-model="dto.activityStatus" size="normal" @change="getActivities()">
          <el-radio-button :label="null">全部</el-radio-button>
          <el-radio-button :label="0">未发布</el-radio-button>
          <el-radio-button :label="1">已发布</el-radio-button>
          <el-radio-button :label="2">已失效</el-radio-button>
        </el-radio-group>
      </div>
      <div class="date-select">
        <div class="date-time-label">创建时间</div>
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
        <el-input @change="getActivities()" v-model="dto.keyword" placeholder="关键字搜索" size="normal" clearable prefix-icon="el-icon-search"></el-input>
      </div>
    </div>
    <div class="activity-table">
      <el-table :data="activities" border stripe fit>
        <el-table-column v-for="col in columns"
          :prop="col.prop"
          :key="col.prop"
          :label="col.label">
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button-group>
              <el-button v-if="scope.row.activityStatus == 1 || scope.row.activityStatus == 0"  size="default" @click="goActivity(scope.row.activityId)">修改</el-button>
              <el-button v-if="scope.row.activityStatus == 0"  size="default" @click="deployActivity(scope.row)">发布</el-button>
              <el-button v-if="scope.row.activityStatus != 1" type="danger" size="default" @click="deleteActivity(scope.row.activityId)">删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-pagination
      @current-change="getActivities"
      :current-page="page.pageNum"
      :page-sizes="[15,30,40]"
      :page-size="page.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.totalNum" background
      :pager-count="7">
    </el-pagination>
  </div>
  <el-dialog
    title="发布活动"
    v-model="deployDialog"
    width="30%">
    <span>
      <el-form size="normal" label-width="80px" label-position="right">
        <el-form-item label="过期时间" size="normal" style="margin: 0 auto;width: 50%">
          <el-date-picker v-model="expiredTime" type="date" size="normal" placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
    </span>
    <template #footer>
      <div style="font-size: 14px; color: #ff1100; text-decoration:underline; width:fit-content; cursor: default;">活动一旦发布不允许提前结束</div>
      <el-button @click="deployDialog = false">取消</el-button>
      <el-button type="primary" @click="updateAndDeploy">确认</el-button>
    </template>
  </el-dialog>
  
</template>
<script>
import Const from '@/js/const'
export default {
  data() {
    return {
      dto: {
        activityStatus: null,
      },
      page: {
        pageSize: 20,
        pageNum: 1,
      },
      expiredTime: {},
      activities: [],
      deployId: null,
      deployDialog: false,
      dateTimeRange:[],
      shortcuts: Const.defShortcuts,
      columns: [
        {
          prop: 'activityTitle',
          label: '标题'
        },
        {
          prop: 'content',
          label: '内容'
        },
        {
          prop: 'createTime',
          label: '创建时间'
        },
        {
          prop: 'deployTime',
          label: '发布时间'
        },
        {
          prop: 'expiredTime',
          label: '过期时间'
        },
      ]
    }
  },
  mounted() {
    this.getActivities();
  },
  methods: {
    getActivities(num) {
      if(num) this.page.pageNum = num;
      this.axios.post('/activity/query/shop/page',{
        dto: this.dto,
        page: this.page,
      }).then(resp=>{
        if(resp.data.success) {
          this.activities = resp.data.data.list;
          this.page.totalNum = resp.data.data.totalNum;
        } else {
          this.$message.error(resp.data.message)
        }
      })
    },
    dateTimeRangeChange(newDateRange) {
      if(newDateRange) {
        this.dto.createTimeStart = newDateRange[0];
        this.dto.createTimeEnd = newDateRange[1];
      } else {
        this.dto.createTimeStart = this.dto.createTimeEnd = null;
      }
      this.getActivities();
    },
    goActivity(aid) {
      this.$router.push('/activity?aid='+aid);
    },
    deployActivity(ac) {
      this.deployId = ac.activityId;
      this.expiredTime = ac.expiredTime;
      this.deployDialog = true;
    },
    updateAndDeploy() {
      this.axios.post('/activity/deploy', {
        activityId: this.deployId,
        expiredTime: this.expiredTime,
      }).then(resp=>{
        this.$msg(resp.data.success,resp.data.message);
        if(resp.data.success) {
          this.getActivities();
          this.deployDialog = false;
        }
      })
    },
    deleteActivity(aid) {
      this.axios.get('/activity/delete/'+aid).then(resp=>{
        this.$msg(resp.data.success,resp.data.message);
        if(resp.data.success) {
          this.getActivities();
        }
      })
    }
  }
}
</script>
<style lang="scss">
.activity-panel {
  width: 80%;
  margin: 5% auto;
  .tool-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .status-select {
      text-align: left;
      margin: 15px 0;
    }
    .date-select {
      text-align: left;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      width: 45%;
      margin: 25px 0;
      .date-time-label {
        margin: 15px;
      }
    }
    .search {
      width: 30%;
    }
  }
  .activity-table {
    margin: 20px auto;
  }
  .warning-text {
    color: #ff1100;
    font-size: 12px;
    text-decoration: underline;
    width: fit-content;
    margin-right: 18px;
  }
}
</style>