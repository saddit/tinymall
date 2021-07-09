import installAxios from '../../plugins/axios'
import { createApp } from 'vue'
import App from './App.vue'
import installElementPlus from '../../plugins/element'
import store from '../../store/index'
import router from './router'
import { ElMessage } from 'element-plus'
require("promise.prototype.finally").shim();

const app = createApp(App).use(router).use(store)
installElementPlus(app)
installAxios(app)
app.mount('#app')
app.config.globalProperties.$msg = (success,msg)=>{
  if(success) {
    ElMessage.success(msg);
  } else {
    ElMessage.error(msg);
  }
}
