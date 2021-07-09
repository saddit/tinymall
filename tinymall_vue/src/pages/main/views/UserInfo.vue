<template>
  <div>
    <el-dialog title="修改密码" v-model="changePwd" width="30%">
      <span>
        <el-form
          :model="subject"
          label-width="80px"
          :inline="false"
          size="normal"
        >
          <el-form-item label="旧密码">
            <el-input
              style="width: 70%"
              v-model="subject.oldPrimary"
              placeholder="请输入旧密码"
            ></el-input>
          </el-form-item>
          <el-form-item label="新密码" size="normal">
            <el-input
              style="width: 70%"
              v-model="subject.newPrimary"
              placeholder="请输入新密码"
            ></el-input>
          </el-form-item>
        </el-form>
      </span>
      <template #footer>
        <el-button @click="changePwd = false">取消</el-button>
        <el-button type="primary" @click="updatePwd">修改</el-button>
      </template>
    </el-dialog>
    <el-dialog
      title="注册商铺"
      v-model="regShopDialog"
      width="40%">
      <span>
        <el-form style="width: 60%; margin: 0 auto;" :model="shop" label-width="80px" :inline="false" size="normal">
          <el-form-item label="商店图标" required>
            <el-upload
              class="shop-uploader"
              :action="baseUrl + '/resource/img/upload'"
              :show-file-list="false"
              :limit="1"
              with-credentials
              :on-success="handleIconSuccess">
              <img v-if="shop.shopIcon" :src="shop.shopIcon" class="avatar">
              <i v-else class="el-icon-plus shop-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="店名" required>
            <el-input v-model="shop.shopName"></el-input>
          </el-form-item>
          <el-form-item label="简介" required>
            <el-input v-model="shop.shopDesc" placeholder="简单介绍你的店铺" size="normal" clearable type="textarea"></el-input>
          </el-form-item>
          <el-form-item label="店铺分类" size="normal" required>
            <el-select v-model="shop.categoryId" style="width:100%;">
              <el-option v-for="item in categories"
                :key="item.categoryId"
                :label="item.categoryDesc"
                :value="item.categoryId">
              </el-option>
            </el-select>
          </el-form-item>     
        </el-form>
      </span>
      <template #footer>
        <el-button @click="regShopDialog= false; shop={}">Cancel</el-button>
        <el-button type="primary" @click="regShop">OK</el-button>
      </template>
    </el-dialog>
    
  </div>
  <div style="width: 80%; margin: 0 auto">
    <el-row gutter="40">
      <el-col class="left-content" :span="5">
        <el-form>
          <el-form-item>
            <el-upload
              class="avatar-uploader"
              :action="baseUrl + '/resource/img/upload'"
              :show-file-list="false"
              with-credentials
              :on-success="uploadAvatarSuccess"
            >
              <el-avatar
                v-if="userInfo.avatar"
                icon="el-icon-user-solid"
                shape="circle"
                :src="userInfo.avatar"
                fit="fit"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <div class="control">
            <el-form-item v-if="userInfo.roleId == 3">
              <el-button
                size="default"
                icon="el-icon-s-shop"
                @click="toAdmin"
                >我的店铺</el-button
              >
            </el-form-item>
            <el-form-item>
              <el-button
                size="default"
                icon="el-icon-s-order"
                @click="$router.push('/orderlist')"
                >我的订单</el-button
              >
            </el-form-item>
            <el-form-item>
              <el-button
                size="default"
                icon="el-icon-location"
                @click="$router.push('/address')"
                >地址管理</el-button
              >
            </el-form-item>
            <el-form-item>
              <el-button
                size="default"
                icon="el-icon-shopping-cart-1"
                @click="$router.push('/shopcar')"
                >购物车</el-button
              >
            </el-form-item>
            <el-form-item>
              <el-button
                size="default"
                icon="el-icon-collection"
                @click="$router.push('/collection')"
                >收藏夹</el-button
              >
            </el-form-item>
            <el-form-item>
              <el-badge :value="noReadNoticeCount" :max="99" :is-dot="false" :hidden="noReadNoticeCount == 0" type="primary">
                <el-button
                  size="default"
                  icon="el-icon-bell"
                  @click="$router.push('/noticelist')"
                  >所有通知</el-button
                >
              </el-badge>
            </el-form-item>
            <el-form-item>
              <el-button
                size="default"
                icon="el-icon-switch-button"
                @click="logout"
                >退出登录</el-button
              >
            </el-form-item>
            <el-form-item v-if="userInfo.roleId == 1">
              <el-button
                type="text"
                size="default"
                @click="regShopDialog = true; getCategories();"
                >我要当店长</el-button
              >
            </el-form-item>
          </div>
        </el-form>
      </el-col>
      <el-col class="right-content" :span="14">
        <el-form :model="userInfo" label-width="80px">
          <el-form-item label="用户名">
            <el-input v-model="userInfo.username" readonly> </el-input>
          </el-form-item>
          <el-form-item label="手机号" size="normal">
            <el-input
              v-model="userInfo.phone"
              size="normal"
              readonly
            ></el-input>
          </el-form-item>
          <el-form-item label="邮箱" size="normal">
            <el-input
              v-model="userInfo.email"
              size="normal"
              readonly
            ></el-input>
          </el-form-item>
          <el-form-item label="昵称" size="normal">
            <el-input
              v-model="userInfo.nickName"
              placeholder="昵称"
              size="normal"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item label="个人简介" size="normal">
            <el-input
              type="textarea"
              v-model="userInfo.userDesc"
              size="normal"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updateUserInfo"
              >更新资料</el-button
            >
            <el-button type="text" @click="changePwd = true"
              >修改密码</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import Const from "@/js/const"
export default {
  data() {
    return {
      noReadNoticeCount: 0,
      regShopDialog: false,
      shop: {},
      baseUrl: Const.baseUrl,
      changePwd: false,
      userInfo: {},
      categories: [],
      subject: {
        oldPrimary: "",
        newPrimary: "",
        oldCode: "",
        newCode: "",
      },
    };
  },
  mounted() {
    this.getUserInfo();
    this.getNoReadNoticeCount();
  },
  methods: {
    getNoReadNoticeCount() {
      this.axios.get('/notice/count').then(resp=>{
        if(resp.data.success) {
          this.noReadNoticeCount = resp.data.data;
        }
      })
    },
    handleIconSuccess(res) {
      this.shop.shopIcon = res.data;
    },
    getCategories() {
      this.axios.get('/category/all').then(resp=>{
        if(resp.data.success) {
          this.categories = resp.data.data;
        }
      })
    },
    regShop() {
      this.axios.post('/shop/register',this.shop).then(resp=>{
        this.$msg(resp.data.success,resp.data.message);
        if (resp.data.success) {
          this.logout();
        }
      })
    },
    getUserInfo() {
      this.axios.get("/user/query").then((resp) => {
        if (resp.data.success) {
          this.userInfo = resp.data.data;
        } else {
          this.$message.error(resp.data.message);
        }
      });
    },
    updateUserInfo() {
      this.axios.post("/user/update", this.userInfo).then((resp) => {
        this.$message.info(resp.data.message);
      });
    },
    updatePwd() {
      this.axios.post("/user/update/pwd", this.subject).then((resp) => {
        this.$message.info(resp.data.message);
        if(resp.data.success) {
          this.changePwd = false;
          this.subject = {};
        }
      });
    },
    uploadAvatarSuccess(resp) {
      if(resp.success) {
        this.userInfo.avatar = resp.data;
        this.axios
          .post("/user/update", {
            avatar: resp.data,
          })
          .then((res) => {
            if (res.data.success) {
              this.$message.success(res.data.message);
            }
          });
      } else {
        this.$message.error(resp.message);
      }
    },
    toAdmin() {
      window.location = '/admin'
    },
    logout() {
      localStorage.clear();
      window.location = '/'
      this.axios.get('/auth/logout');
    }
  },
};
</script>
<style lang="scss">
.left-content {
  .el-button {
    width: 70%;
  }
  .el-badge {
    width: 70%;
    .el-button {
      width: 100%;
    }
  }
  .control {
    padding-top: 30px;
    padding-bottom: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 5px;
  }
  .el-avatar {
    width: 250px;
    height: 250px;
    margin-bottom: 5%;
  }
  .avatar-uploader {
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      line-height: 250px;
      text-align: center;
    }
    .el-upload {
      width: 250px;
      height: 250px;
      border: 1px dashed #d9d9d9;
      border-radius: 50%;
      cursor: pointer;
      position: relative;
    }
  }
}
.shop-uploader {
  .shop-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    line-height: 120px;
    text-align: center;
  }
  .el-upload {
    width: 120px;
    height: 120px;
    border: 1px dashed #d9d9d9;
    cursor: pointer;
    position: relative;
  }
}
.right-content {
  padding-top: 10%;
  .el-form-item {
    width: 60%;
    margin: 40px auto;
    .el-input {
      width: 60%;
    }
    .el-textarea {
      width: 70%;
    }
  }
  .el-button {
    margin: 10px;
  }
  overflow: hidden;
}
</style>