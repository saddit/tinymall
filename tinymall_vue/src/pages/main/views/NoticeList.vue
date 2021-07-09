<template>
  <div class="notice-list">
    <div class="tool-bar">
      <div class="status-select">
        <el-radio-group v-model="dto.isRead" size="normal" @change="getNotices">
          <el-radio-button :label="null">全部</el-radio-button>
          <el-radio-button :label="true">已读</el-radio-button>
          <el-radio-button :label="false">未读</el-radio-button>
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
      <div class="opt-btns">
        <el-button-group>
          <el-button  size="default" @click="deleteNotice('all',true)">删除已读</el-button>
          <el-button  size="default" @click="readNotice('all')">已读全部</el-button>
        </el-button-group>
      </div>
    </div>
    <div>
      <el-table :data="notices" border stripe fit>
        <el-table-column v-for="col in columns"
          :prop="col.prop"
          :key="col.prop"
          :label="col.label">
        </el-table-column>
        <el-table-column label="已读">
          <template #default="scope">
            {{scope.row.isRead ? '是':'否'}}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" size="default" @click="deleteNotice(scope.row.noticeId)">删除</el-button>
            <el-button v-if="!scope.row.isRead" type="primary" size="default" @click="readNotice(scope.row.noticeId)">已读</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        hide-on-single-page
        @current-change="getNotices"
        :current-page="page.pageNum"
        :page-size="page.pageSize"
        layout="prev, pager, next, jumper"
        :page-count="page.totalPage" background
        :pager-count="7">
      </el-pagination>
      
    </div>
  </div>
</template>
<script>
import Const from "@/js/const"
export default {
  data() {
    return {
      dto:{
        isRead: null,
      },
      notices: [],
      page:{
        pageNum: 1,
        pageSize: 10
      },
      dateTimeRange:[],                  
      shortcuts: Const.defShortcuts,                    
      columns: [
        {
          prop: 'noticeTitle',
          label: '标题',
        },                              
        {
          prop: 'content',
          label: '内容',
        },
        {
          prop: 'createTime',
          label: '创建时间'
        },
        {
          prop: 'deployUsername',
          label: '发布者'
        }
      ]
    }
  },
  mounted() {
    this.getNotices();
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
    getNotices(num) {
      if(num) {
        this.page.pageNum = num;
      }
      this.axios.post('/notice/query/page',{
        dto: this.dto,
        page: this.page
      }).then(resp=>{
        if(resp.data.success) {
          this.notices = resp.data.data.list;
          this.page = resp.data.data;
          this.page.list = null
        }
      })
    },
    deleteNotice(option,isRead='') {
      var path = "/notice/delete/" + option + '/' + isRead;
      this.axios.get(path).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        if(resp.data.success) {
          this.getNotices();
        }
      })
    },
    readNotice(option) {
      var path = "/notice/read/"+ option
      this.axios.get(path).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        if(resp.data.success) {
          this.getNotices();
        }
      })
    }
  }
}
</script>
<style lang="scss">
.notice-list {
  width: 80%;
  margin: 5% auto;
  .tool-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .status-select {
      text-align: left;
      margin: 15px 0;
      widows: 33%;
    }
    .date-select {
      text-align: left;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      width: 37%;
      margin: 25px 0;
      .date-time-label {
        margin: 15px;
      }
    }
    .opt-btns {
      width: 30%;
      margin: 0 50px;
    }
  }
}
</style>