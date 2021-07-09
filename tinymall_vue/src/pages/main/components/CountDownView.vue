<template>
  <div class="count-down">
    <span class="s-text">剩 </span>
    <i class="el-icon-time t-icon" />{{label}}
    <span class="s-text">{{text}}</span>
  </div>
</template>
<script>
export default {
  props: {
    startDate: ()=>new Date(),
    intervalDay: ()=> 0,
    text: ()=>''
  },
  data() {
    return {
      label: "",
    };
  },
  mounted() {
    var endDate = new Date(this.startDate.getTime());
    endDate.setDate(this.startDate.getDate() + this.intervalDay);
    var times = endDate.getTime() - new Date().getTime();

    this.starCountDown(times * 0.001);
  },
  methods: {
    getTimeLabel(times) {
      var day = 0,hour = 0,minute = 0,second = 0; //时间默认值
      if (times > 0) {
        day = Math.floor(times / (60 * 60 * 24));
        hour = Math.floor(times / (60 * 60)) - day * 24;
        minute = Math.floor(times / 60) - day * 24 * 60 - hour * 60;
        // second = Math.floor(times) - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60;
      }
      if (day <= 9) day = "0" + day;
      if (hour <= 9) hour = "0" + hour;
      if (minute <= 9) minute = "0" + minute;
      // if (second <= 9) second = "0" + second;
      
      return day + "天" + hour + "时" + minute + "分";
    },
    starCountDown(times) {
      this.label = this.getTimeLabel(times);
      setInterval(()=>{
        this.label = this.getTimeLabel(times);
        times -= 60;
      },1000 * 60);
    }
  }
};
</script>
<style lang="scss">
.count-down {
  .t-icon {
    color: inherit;
    font-size: inherit;
  }
  .s-text {
    color: inherit;
    font-size: 11px;
  }
  width: fit-content;
  color: #ff4111;
  font-size: 15px;
}
</style>