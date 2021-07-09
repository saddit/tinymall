<template>
  <div class="shop-car">
    <div class="title">
      <el-checkbox v-model="isCarChecked" @change="handleSelectCar"/>
      <el-image
        class="title-icon"
        :src="car.shopIcon"
        fit="fill"
        :lazy="true"
      />
      {{ car.shopName }}
    </div>
    <div class="shop-car-item" v-for="item in car.items" :key="item.productId">
      <el-row :gutter="20" type="flex" justify="start">
        <el-col class="product-img" :span="6" :offset="0">
          <el-checkbox v-model="item.checked" @change="(checked)=>handleSelectItem(checked,car,item)" />
          <el-image
            :src="item.productImg"
            fit="fill"
            :lazy="true"
          />
        </el-col>
        <el-col :span="16" :offset="0">
          <div class="item-name">{{ item.productName }}</div>
          <div class="item-type">
            <el-select-v2
              v-model="item.productType"
              :options="typeOption[item.productId]"
              size="mini"
              @visible-change="(visible) => getType(visible, item.productId)"
            />
            <el-button type="text" size="small" @click="handleDeleteItem(car,item)">移除</el-button> 
          </div>
          <el-row :gutter="20">
            <el-col :span="10" :offset="1">
              <el-input-number
                v-model="item.productSize"
                :min="1"
                size="mini"
                @change="(n,o) => handleChangeSize(n,o,car,item)"
              />
            </el-col>
            <el-col :span="10" :offset="3">
              <div class="price-text">￥{{ getPrice(item) }}</div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="2" :offset="0"></el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      typeOption: {},
      isInitOption: {},
      isCarChecked: false
    };
  },
  emits: ['select-item','delete-item'],
  props: ['car'],
  created() {
    this.car.items.forEach((p) => {
      p.checked = false;
      this.typeOption[p.productId] = [
        {
          value: p.productType,
          label: p.productType,
        },
      ];
      this.isInitOption[p.productId] = false;
    });
  },
  methods: {
    getPrice(item) {
      return Number.parseFloat(item.productPrice) * Number.parseInt(item.productSize);
    },
    getType(visable, pid) {
      if (visable && !this.isInitOption[pid]) {
        this.axios.get("/product/query/"+pid).then(resp=>{
          if(resp.data.success) {
            var ops = resp.data.data.productTypes.split(',');
            this.typeOption[pid] = Array.from({ length: ops.length }).map(
              (_, idx) => ({
                value: ops[idx],
                label: ops[idx],
              })
            );
            this.isInitOption[pid] = true;
          }
        })
      }
      this.options = this.typeOption[pid];
    },
    handleSelectCar(checked) {
      this.car.items.forEach(i=>{ 
        if(i.checked !== checked) {
          i.checked = checked
          this.handleSelectItem(checked,this.car,i)
        }
      })
    },
    handleDeleteItem(car,item) {
      this.$emit('delete-item',car,item)
    },
    handleSelectItem(checked,car,item) {
      if (checked == true) {
        if(!this.isCarChecked) {
          var count = 0
          this.car.items.forEach(i=> i.checked ? count++ : 0)
          if (count == this.car.items.length) {
            this.isCarChecked = true
          }
        }
      } else {
        this.isCarChecked = false;
      }
      this.$emit('select-item', checked, car, item)
    },
    handleChangeSize(n,o,car,item) {
      if(item.checked) {
        item.productSize = o;
        this.$emit('select-item',false, car, item);
        item.productSize = n;
        this.$emit('select-item',true,car, item);
      }
    }
  }
};
</script>
<style lang="scss">
.shop-car {
  .title {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    margin: 5% 0;
    .title-icon {
      margin: 0 15px;
      height: 40px;
      width: 40px;
    }
  }
  .product-img {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    .el-image {
      margin-left: 5%;
    }
  }
  .shop-car-item {
    margin: 5% auto;
    .price-text {
      color: #ff2111;
      font-size: 20px;
    }
  }
  .item-name {
    margin: 4% 12%;
    text-align: left;
  }
  .item-type {
    margin-bottom: 3%;
    margin-left: 11%;
    width: 80%;
    display: flex;
    justify-content: space-between;
    .el-button {
      width: 20%;
    }
    .el-select-v2 {
      width: 33%;
    }
    .el-select-v2__placeholder {
      left: 0.5px;
      font-size: 13px;
    }
  }
}
</style>