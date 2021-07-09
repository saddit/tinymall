<template>
  <div class="main-content" v-if="activities.length > 0">
    <div class="event-content" v-for="item in activities" :key="item.activityId">
      <event-view
        :id="item.activityId"
        :title="item.activityTitle"
        :desc="item.content"
        :img-src="item.previewImg"
        @click="goSearch(item.activityId)"
      />
    </div>
    <el-pagination
        hide-on-single-page
        @current-change="getActivies"
        :current-page="page.pageNum"
        :page-size="page.pageSize"
        layout="prev, pager, next, jumper"
        :page-count="page.totalPage" background
        :pager-count="7">
      </el-pagination>
  </div>
  <div v-else>
    <el-empty description="暂时没有任何活动哦，到首页逛逛吧"></el-empty>
  </div>
</template>
<script>
import EventView from '../components/EventView.vue'
export default {
  data(){
    return {
      activities:[],
      dto: {
        activityStatus: 1,
      },
      page: {
        pageNum: 1,
        pageSize: 5,
        orderBy: 'create_time'
      }
    }
  },
  components: {EventView},
  mounted() {
    this.getActivies();
  },
  methods: {
    getActivies(page) {
      if(page) {
        this.page.pageNum = page;
      }
      this.axios.post('/activity/query/page',{
        dto: this.dto,
        page: this.page
      }).then(resp=>{
        if(resp.data.success) {
          this.activities = resp.data.data.list;
          this.page = resp.data.data;
          this.page.list = null;
        }
      })
    },
    goSearch(id) {
      this.$router.push('/search?aid='+id);
    }
  }
}
</script>
<style lang="scss">
.main-content {
  padding: 50px;
}
.event-content {
  width: 60%;
  margin: 30px auto;
}
</style>