import { createStore } from 'vuex'
import Const from '../js/const'

var data = localStorage.getItem(Const.userInfoKey)
let preUserInfo;
try {
  preUserInfo = JSON.parse(data) || {};
} catch (SyntaxError) {
  preUserInfo = {};
}

export default createStore({
  state: {
    token: preUserInfo.token || null,
    userId: preUserInfo.userId || null,
    username: preUserInfo.userId || null,
    roleId: preUserInfo.roleId || null,
  },
  mutations: {
    setUserInfo(state, info) {
      localStorage.setItem(Const.userInfoKey, JSON.stringify(info))
      state.token = info.token;
      state.userId = info.userId;
      state.username = info.username;
      state.roleId = info.roleId;
    },
  },
  actions: {
  },
  modules: {
  }
})
