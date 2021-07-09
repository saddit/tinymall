<template>
  <div class="product-panel">
    <el-form :model="product" label-width="100px" label-position="left" :inline="false" size="normal">
      <el-form-item class="t-align-left ma-h-15p" label="商品名称" required>
        <el-input class="p-60" v-model="product.productName" :max="40"></el-input>
      </el-form-item>
      <el-form-item class="t-align-left ma-h-15p" label="商品简介" size="normal" required>
        <el-input
          class="p-60"
          type="textarea"
          v-model="product.productDesc"
          placeholder="简单介绍你的商品"
          size="normal"
          clearable
          :max="150"
          :autosize="{ minRows: 3, maxRows: 4 }"
        ></el-input>
      </el-form-item>
      <el-form-item class="t-align-left ma-h-15p" label="公司" size="normal" required>
        <el-input
          class="p-60"
          v-model="product.productCompany"
          placeholder="发行公司名称"
          size="normal"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item class="t-align-left ma-h-15p" label="商品分类" size="normal" required>
        <el-select v-model="product.categoryId">
          <el-option
            v-for="item in categories"
            :key="item.categoryId"
            :label="item.categoryDesc"
            :value="item.categoryId"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="t-align-left ma-h-15p" label="标签" size="normal" required>
        <el-select
          v-model="product.productTags"
          multiple
          filterable
          allow-create
          default-first-option
          @change="handleTagChange"
          placeholder="请添加商品标签"
        >
        </el-select>
      </el-form-item>
      <el-form-item class="t-align-left ma-h-15p" label="库存量" size="normal" required>
        <el-input-number v-model="product.stock" size="normal"
          :min="0" :step="1" :controls="true" controls-position="right">
        </el-input-number>
      </el-form-item>
      <el-form-item class="t-align-left ma-h-15p" label="价格" required>
        <el-input-number v-model="product.salePrice" size="normal" placeholder="销售价"
          :min="1" :precision="2" :controls="false">
        </el-input-number>
        <el-input-number class="ma-h-15p" v-model="product.originalPrice" size="normal" placeholder="原价"
          :min="1" :precision="2" :controls="false">
        </el-input-number>
      </el-form-item>
      <el-form-item class="t-align-left ma-h-15p" label="款式" size="normal" required>
        <el-select
          v-model="product.productTypes"
          multiple
          allow-create
          filterable
          default-first-option
          placeholder="请添加商品款式"
        >
        </el-select>
      </el-form-item> 
      <el-form-item class="ma-h-15p" label="图集" required>
        <el-upload
          class="upload-img"
          :action="baseUrl + '/resource/img/upload'"
          :on-success="onUploadSuccess"
          :file-list="picList"
          :on-preview="selectPreview"
          :limit="5"
          :accept="['jpg','png','jpeg','gif']"
          with-credentials
          list-type="picture">
        <el-button size="small">点击上传</el-button>
        <template #tip>
          <div class="el-upload__tip">
            只能上传图片，且不超过 1MB
          </div>
        </template>
      </el-upload>
        <div class="prev-img-name">已选择预览图：{{previewFile.name || '无'}}</div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import Const from "@/js/const"
import Api from "@/js/api"
export default {
  data() {
    return {
      baseUrl: Const.baseUrl,
      isNewAdd: true,
      previewFile: {},
      product: {
        productTypes: [],
        productTags: [],
        productPic: [],
      },
      picList: [],
      categories: [],
    };
  },
  mounted() {
    this.getCategories();
    var pid = this.$route.query.pid;
    if(pid) {
      this.isNewAdd = false;
      this.getProduct(pid)
    }
  },
  methods: {
    getProduct(pid) {
      this.axios.get("/product/query/" + pid).then((resp) => {
        if (resp.data.success) {
          this.product = resp.data.data;
          this.product.productTypes = this.product.productTypes.split(',');
          this.product.productTags = this.product.productTags.split(',');
          this.product.productPic = this.product.productPic.split(',');
          this.getPicList();    
        } else {
          this.$message.error(resp.data.message);
        }
      });
    },
    getPicList() {
      var urls = this.product.productPic
      this.picList = urls.map((url,i) => {
        var item = {
          name: 'pic-'+i,
          url: url
        }
        if(url == this.product.previewImg) {
          this.previewFile = item;
        }
        return item;
      })
    },
    onUploadSuccess(resp) {
      this.picList.push({
        name: Api.getLocalDateTime(),
        url: resp.data
      })
      this.product.productPic.push(resp.data);
    },
    selectPreview(file) {
      this.product.previewImg = file.url;
      this.previewFile = file;
    },
    getCategories() {
      this.axios.get('/category/all')
        .then(resp => {
          if(resp.data.success) {
            this.categories = resp.data.data;
          }
        })
    },
    onSubmit() {
      this.product.productTags = this.product.productTags.join(",");
      this.product.productTypes = this.product.productTypes.join(",");
      this.product.productPic = this.product.productPic.join(",");
      var path = this.isNewAdd ? '/product/add' : '/product/update';
      this.axios.post(path,this.product).then(resp=>{
          this.$msg(resp.data.success,resp.data.message);
          if(resp.data.success && this.isNewAdd) {
            this.$router.push('/productlist');
          }
        }).finally(()=>{
          this.product.productTypes = this.product.productTypes.split(',');
          this.product.productTags = this.product.productTags.split(',');
          this.product.productPic = this.product.productPic.split(',');
        })
    },
  },
};
</script>
<style lang="scss">
.product-panel {
  width: 60%;
  margin: 0 auto;
  .p-60 {
    width: 60%;
  }
  .ma-h-15p {
    margin-left: 15px;
    margin-right: 15px;
  }
  .t-align-left {
    text-align: left;
  }
  .prev-img-name {
    font-family: inherit;
    font-size: 14px;
    color: #ff7700;
    padding: 10px;
    border: 1px dashed #c0ccda;
    border-radius: 6px;
    width: fit-content;
    margin: 10px auto;
  }
  .upload-img {
    width: 60%;
    margin: 0 auto;
  }
}
</style>