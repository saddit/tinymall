<template>
  <div style="margin: 0 auto; width: 60%;" v-loading="loadding">
    <el-row :gutter="20">
      <el-col :span="11" :offset="0">
        <div class="pic-box">
          <div class="img">
            <el-carousel height="300px" type="default" direction="horizontal"
              :autoplay="true" :interval="2000" :loop="true" :initial-index="1"
              trigger="hover" indicator-position="none" arrow="hover" ref="pics">
              <el-carousel-item v-for="pic,i in pics" :key="i">
                 <el-image 
                  :src="pic" 
                  fit="fit" 
                  :lazy="false" />
              </el-carousel-item>
            </el-carousel>
          </div>
          <div v-for="(pic,idx) in pics" :key="idx" class="img" @click="$refs.pics.setActiveItem(idx)">
            <el-image :src="pic" fit="fill" :lazy="true"></el-image>
          </div>
        </div>
      </el-col>
      <el-col :span="12" :offset="0">
        <div class="product-name">
          {{product.productName}}
        </div>
        <div class="product-desc">
          {{product.productDesc}}
        </div>
        <div class="coupon-list">
          <div class="coupon-label">优惠</div>
          <div class="coupon-item" v-for="coupon in coupons" :key="coupon.couponId" @click="receiveCoupon(coupon.couponId)">
            <i class="el-icon-s-ticket c-icon"/>{{getCouponText(coupon)}}
          </div>
        </div>
        <div class="type-select">
          <div class="type-title">款式:</div>
          <el-radio-group v-model="selectedType">
            <el-radio-button v-for="t,i in types" :key="i" :label="t"></el-radio-button>
          </el-radio-group>
        </div>
        <div class="size-price-content">
          <div class="price-label">数量：</div>
          <el-input-number v-model="selectedSize" size="normal"
            :min="1" :step="1" :controls="true" controls-position="right" />
          <div class="price-label">
            单价:
          </div>
          <div class="price-sale">
            ￥{{product.salePrice}}
          </div>
          <div class="price-original">
            ￥{{product.originalPrice}}
          </div>
        </div>
        <div class="p-opt-btns">
          <el-button icon="el-icon-collection" size="default" @click="addToColl">收藏</el-button>
          <el-button icon="el-icon-shopping-cart-2" size="default" @click="addToCar">加购</el-button>
          <el-button icon="el-icon-goods" type="primary" size="default" @click="orderSettle">购买</el-button>
        </div>
      </el-col>
      <el-col :span="1" :offset="0"></el-col>  
    </el-row>
    <div v-html="product.introPage"></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      product: {},
      coupons: [],
      selectedImg: '',
      selectedType: '',
      selectedSize: 1,
      pics: [],
      types: [],
      shop: {},
      loadding: true,
    }
  },
  mounted() {
    var pid = this.$route.query.pid;
    if(pid) {
      this.getProduct(pid);
      
    } else {
      this.$router.push('/');
    }
  },
  methods: {
    getCouponText(c) {
      if(c.couponType == 0) {
        if(c.couponStep == 0) {
          return '立减'+ c.couponValue;
        } else {
          return '满'+c.couponStep+'减'+c.couponValue;
        }
      } else {
        if(c.couponStep == 0) {
          return c.couponValue + '折券';
        } else {
          return '满'+c.couponStep+'打'+c.couponValue+'折';
        }
      }
    },
    getProduct(pid) {
      this.axios.get("/product/query/"+pid).then(resp=>{
        if(resp.data.success) {
          this.product = resp.data.data;
          this.pics = this.product.productPic.split(',');
          this.types = this.product.productTypes.split(',');
          this.selectedType = this.types[0];
          this.$refs.pics.setActiveItem(0);
          this.getShopInfo(this.product.shopId);
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    getShopInfo(sid) {
      this.axios.get('/shop/query/'+sid).then(resp=>{
        if(resp.data.success) {
          this.shop = resp.data.data;
          this.getCoupons(this.shop.activityId);
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    getCoupons(aid) {
      this.axios.get("/coupon/query/activity/"+aid).then(resp=>{
        if(resp.data.success) {
          this.coupons = resp.data.data;
          this.loadding = false;
        } else {
          this.$message.error(resp.data.message);
        }
      })
    },
    receiveCoupon(cid) {
      this.axios.get('/coupon/receive/'+cid).then(resp=>{
        if(resp.data.success) {
          this.$message.success(resp.data.message);
        } else {
          if(resp.data.code == 403){
            this.$message.warning('请先登录');
          } else {
            this.$message.error(resp.data.message);
          }
        }
      })
    },
    addToColl() {
      this.axios.get('/collection/add/'+this.product.productId).then(resp=>{
        if(resp.data.success) {
          this.$message.success(resp.data.message);
        } else {
          if(resp.data.code == 403){
            this.$message.warning('请先登录');
          } else {
            this.$message.error(resp.data.message);
          }
        }
      })
    },
    addToCar() {
      this.axios.post('/shopcar/add/',{
        productId: this.product.productId,
        productSize: this.selectedSize,
        productType: this.selectedType,
      }).then(resp=>{
        if(resp.data.success) {
          this.$message.success(resp.data.message);
        } else {
          if(resp.data.code == 403){
            this.$message.warning('请先登录');
          } else {
            this.$message.error(resp.data.message);
          }
        }
      })
    },
    orderSettle() {
      this.$router.push({
        name: 'ordersettle',
        params: {
          data: JSON.stringify([{
            productId: this.product.productId,
            productName: this.product.productName,
            productImg: this.product.previewImg,
            productPrice: this.product.salePrice,
            productType: this.selectedType,
            productSize: this.selectedSize,
            shopId: this.shop.shopId,
            shopIcon: this.shop.shopIcon,
            shopName: this.shop.shopName
        }])
        }
      });
    }
  }
}
</script>
<style lang="scss">
.pic-box{ 
  width: 445px; 
  overflow: hidden;
  .img{
    float: right;
    width: 125px;
    height: 125px;
    margin: 0 10px 30px 0;
    .el-image {
      width: 100%;
      height: 100%;
    }
    &:first-child {
      float: left;
      width: 300px; 
      height: 300px;
      margin: 0 10px 10px 0;
    }
    &:nth-child(5),&:nth-child(6) {
      margin: 0 30px 10px 0;
    }  
  }
}
.product-name {
  font-size: 20px;
  font-weight: bold;
  margin: 25px 0;
  text-align: left;
}
.product-desc {
  font-size: 14px;
  color: gray;
  margin: 10px 0;
  text-align: left;
}
.type-select {
  .type-title {
    font-weight: bold;
    margin: 10px 0;
    font-size: 15px;
  }
  text-align: left;
  margin-top: 4%;
  margin-bottom: 5%;
}
.p-opt-btns {
  margin: 10% 0;
  .el-button {
    margin: 0 10px;
  }
}
.coupon-list {
  margin-top: 5%;
  font-size: 14px;
  text-align: left;
  display: flex;
  justify-content: flex-start;
  height: 10%;
  border: 1px solid #efefef;
  border-left: none;
  border-right: none;
  padding: 5px 0;
  width: 90%;
  overflow: hidden;
  .coupon-label {
    color: gray;
    font-size: 14px;
    margin-right: 15px;
    font-weight: bold;
  }
  .coupon-item {
    cursor: pointer;
    margin-right: 10px;
    margin-bottom: 6px;
    border: 1px dashed #ff8800;
    padding: 2px;
    height: fit-content;
    border-radius: 5px;
    &:hover {
      opacity: 80%;
    }
    .c-icon {
      color: #ff0000; 
      margin-right: 3px;
    }
  }
}
.size-price-content {
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
  margin: 25px 0;
  .el-input-number {
    margin-right: 15%;
  }
  .price-label {
    font-size: 12px;
    color: gray;
  }
  .price-sale,.price-original {
    color: #ff0000;
  }
  .price-sale {
    font-size: 25px;
    padding: 0 5px;
  }
  .price-original {
    font-size: 16px;
    padding: 0 5px;
    text-decoration: line-through;
  }

}
</style>