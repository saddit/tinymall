import installAxios from '../../plugins/axios'
import { createApp } from 'vue'
import App from './App.vue'
import installElementPlus from '../../plugins/element'
import store from '../../store/index'
import router from './router'

const app = createApp(App).use(router).use(store)
installElementPlus(app)
installAxios(app)
app.mount('#app')
