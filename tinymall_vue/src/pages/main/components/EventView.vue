<template>
  <div>
    <el-card class="event-card" @click="$emit('click', id)" shadow="always" :body-style="{ padding: '20px' }">
      <template #header>
        <el-image :src="imgSrc" fit="fill" :lazy="true"></el-image>
      </template>
      <h1 style="text-align: left;">{{title}}</h1>
      <p style="text-align:left;">{{desc}}</p>
    </el-card>
  </div>
</template>
<script>
export default {
  props: {
    id: {
      type: Number
    },
    imgSrc: ()=> '',
    title: {
      type: String
    },
    desc: {
      type: String
    }
  },
  emits: ['click']
}
</script>
<style lang="scss">
.event-card {
  width: 1024px;
  height: fit-content;
}
</style>