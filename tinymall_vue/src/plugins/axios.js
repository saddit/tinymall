"use strict";

import axios from "axios";
import VueAxios from 'vue-axios'
import Const from '../js/const'
import store from '../store/index'

// Full config:  https://github.com/axios/axios#request-config
// axios.defaults.baseURL = process.env.baseURL || process.env.apiUrl || '';
// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

let config = {
  baseURL: process.env.baseURL || process.env.apiUrl || Const.baseUrl,
  timeout: 3 * 60 * 1000, // Timeout
  withCredentials: true, // Check cross-site Access-Control
};

const _axios = axios.create(config);

_axios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    if(store.state.token) {
      config.headers['token'] = store.state.token; 
    }
    return config;
  },
  function(error) {
    // Do something with request error
    console.log(error);
    return Promise.reject(error);
  }
);

// Add a response interceptor
_axios.interceptors.response.use(
  function(response) {
    // Do something with response data
    if(response.data.code == '4031') {
      localStorage.clear();
      window.location = '/login';
    }
    return response;
  },
  function(error) {
    // Do something with response error
    console.log(error);
    return Promise.reject(error);
  }
);

export default (app)=>{
  app.use(VueAxios, _axios)
};
