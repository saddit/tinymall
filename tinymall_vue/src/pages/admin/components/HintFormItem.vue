<template>
  <el-form-item :label="label" size="normal" :required="required">
    <div class="hint-fi">
      <slot></slot>
      <div class="hint-text">
        <slot name="hint">
          {{hint}}
        </slot>
      </div>
    </div>
  </el-form-item>
</template>
<script>
export default {
  props: {
    label:()=>'',
    hint:()=>'',
    required: ()=>false
  }
}
</script>
<style lang="scss">
 .hint-fi {
  display: flex;
  justify-content: flex-start;
  .hint-text {
    color: gray;
    font-size: 12px;
    text-decoration: underline;
    width: fit-content;
    cursor: default;
    margin-left: 20px;
  }
} 
</style>