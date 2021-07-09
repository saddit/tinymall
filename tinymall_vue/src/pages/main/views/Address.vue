<template>
  <div style="margin: 0 auto; width:30%;">
    <el-radio-group v-model="defaultAddrId" @change="changeDefaultAddr" style="width: 100%">
      <div v-if="addrs.length == 0">
        <el-button type="text" size="default" disabled>暂无地址信息</el-button>
      </div>
      <address-view
      v-for="addr in addrs"
      :key="addr.addressId"
      :address="addr"
      @delete="deleteAddr"
      @update="updateAddress"
      >
        <template #footer>
          <el-radio :label="addr.addressId">默认地址</el-radio>
        </template>
      </address-view>
    </el-radio-group>   
    <div style="text-align: right; margin: 25px;">
      <el-button icon="el-icon-add-location" round type="primary" size="default" @click="addNewAddr = true">添加新地址</el-button>
    </div>
  </div> 
  <el-dialog
    :title="updateAddr ? '修改地址' : '添加新地址'"
    v-model="addNewAddr"
    width="40%">
    <span>
      <el-form :model="newAddr" label-width="80px" :inline="true" size="normal">
        <el-form-item label="收货人">
          <el-input v-model="newAddr.consignee" placeholder="真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="newAddr.consigneePhone" placeholder="电话号码"></el-input>
        </el-form-item>
        <el-form-item label="收货地址" class="addr-select">
          <el-select v-model="newAddr.province" @change="selectProvice">
            <el-option 
            v-for="opt in provinceOptions"
            :key="opt.value" 
            :label="opt.label" 
            :value="opt.label" />
          </el-select>省
          <el-select v-model="newAddr.city" @change="selectCity">
            <el-option 
            v-for="opt in cityOptions"
            :key="opt.value" 
            :label="opt.label" 
            :value="opt.label" />
          </el-select>市
          <el-select v-model="newAddr.country">
            <el-option 
            v-for="opt in countryOptions"
            :key="opt.value" 
            :label="opt.label" 
            :value="opt.label" />
          </el-select>县/区
        </el-form-item>
        <el-form-item label="详细地址" size="normal">
          <el-input 
          class="addr-street"
          :autosize="{ minRows: 2, maxRows: 4}" 
          type="textarea" 
          v-model="newAddr.street" 
          placeholder="街道、村落、门牌号"/>
        </el-form-item>
        <el-form-item class="addr-setdef">
          <el-checkbox v-model="newAddr.isDefault" :label="true">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>
    </span>
    <template #footer>
      <el-button @click="addNewAddress">确认</el-button>
      <el-button type="primary" @click="addNewAddr = false; newAddr={};">取消</el-button>
    </template>
  </el-dialog>
</template>
<script>
import AddressView from '../components/AddressView.vue'
import {provinceDataList, cityDataMap, countryDataMap, textCodeMap} from '@/js/area-data'
export default {
  components: { AddressView },
  data() {
    return {
      defaultAddrId: 1,
      addNewAddr: false,
      updateAddr: false,
      newAddr: {},
      provinceOptions: provinceDataList,
      cityOptions: [],
      countryOptions: [],
      addrs: []
    }
  },
  mounted() {
    this.getAddrs();
  },
  methods: {
    changeDefaultAddr() {
      //request
      this.axios.post('/address/update',{
        addressId: this.defaultAddrId,
        isDefault: true,
      }).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
      })
    },
    addNewAddress() {
      var path = this.updateAddr ? '/address/update' : '/address/add'
      this.axios.post(path,this.newAddr).then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        if(resp.data.success) {
          this.addNewAddr = false;
          this.updateAddr = false;
          this.newAddr = {};
          this.getAddrs();
        }
      })
    },
    deleteAddr(id) {
      this.axios.get("/address/delete/"+id)
      .then(resp=>{
        this.$msg(resp.data.success, resp.data.message);
        if(resp.data.success) {
          this.getAddrs();
        }
      })
    },
    getAddrs() {
      //request
      this.axios.get('/address/get').then(resp=>{
        if(resp.data.success) {
          this.addrs = resp.data.data;
          var defAddr = this.addrs.find(i=> i.isDefault);
          this.defaultAddrId = defAddr.addressId;
        }
      })
    },
    updateAddress(addr) {
      this.newAddr = addr;
      this.updateAddr = this.addNewAddr = true;
    },
    selectProvice(item) {
      this.newAddr.city = '';
      this.newAddr.country = '';
      if(item == undefined || item == null || item == "") {
        this.cityOptions = [];
        this.countryOptions = [];
        return;
      }
      let code = textCodeMap.get(item);
      this.cityOptions = cityDataMap.get(code);
    },
    selectCity(item) {
      this.newAddr.country = '';
      if(item == undefined || item == null || item == "") {
        this.countryOptions = [];
        return;
      }
      let code = textCodeMap.get(item);
      this.countryOptions = countryDataMap.get(code);
    }
  }
}
</script>
<style lang="scss">
.addr-select {
  .el-select {
    width: 130px;
    padding-right: 10px;
    padding-left: 10px;
  }
}
.addr-street {
  width: 495px;
  padding: 0 10px;
}
.addr-setdef {
  margin: 0 20px;
  text-align: left;
  width: 465px;
}
</style>