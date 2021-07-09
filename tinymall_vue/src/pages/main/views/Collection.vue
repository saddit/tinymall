<template>
  <div class="coll-toolbar">
    <el-select v-model="dto.categoryId" placeholder="按分类查找" clearable @change="getColls()">
      <el-option v-for="item in categories"
        :key="item.categoryId"
        :label="item.categoryDesc"
        :value="item.categoryId">
      </el-option>
    </el-select>
    
    <el-input style="width: 25%;" v-model="dto.keyword" placeholder="输入关键字搜索" size="normal" clearable @change="getColls()" prefix-icon="el-icon-search"></el-input>
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
  <div v-if="colls.length > 0">
    <div style="width: 40%; margin: 0 auto;" v-loading="loadding">
      <collection-view
        v-for="col in colls"
        :key="col.collId"
        :coll="col"
        @remove="removeColl"
      />
    </div>
    <el-pagination
        hide-on-single-page
        @current-change="getColls"
        :current-page="page.pageNum"
        :page-size="page.pageSize"
        layout="prev, pager, next, jumper"
        :page-count="page.totalPage" background
        :pager-count="7">
      </el-pagination>
  </div>
    <div v-else>
      <el-empty description="你的收藏夹还没有东西哦"></el-empty>
    </div>
</template>
<script>
import CollectionView from '../components/CollectionView.vue'
import Api from '@/js/api'
export default {
  components: {CollectionView},
  data() {
    return {
      dto: {
        categoryId: null,
      },
      page:{
        pageNum: 1,
        pageSize: 10,
      },
      colls: [],
      categories: [],
      loadding: true
    }
  },
  mounted() {
    this.getCategories();
    this.getColls();
  },
  methods: {
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
      this.getColls();
    },
    getCategories() {
      this.axios.get('/category/all').then(resp=>{
        if(resp.data.success) {
          this.categories = resp.data.data;
          this.categories.unshift({
            categoryId: null,
            categoryDesc: '全部'
          })
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    getColls(num) {
      if(num) {
        this.page.pageNum=num;
      }
      this.axios.post('/collection/query/page',{
        dto: this.dto,
        page: this.page
      }).then(resp=>{
        if(resp.data.success) {
          this.colls = resp.data.data.list;
          this.page = resp.data.data;
          this.page.list = null
          this.loadding = false;
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    removeColl(cid) {
      this.axios.get('/collection/delete/'+cid).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        this.getColls();
      })
    }
  }
}
</script>
<style lang="scss">
.coll-toolbar {
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