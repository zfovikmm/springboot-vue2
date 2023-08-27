import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'
import request from "@/utils/request";
import store from "./store";


Vue.use(ElementUI,{size: "small"});

Vue.prototype.request=request

Vue.config.productionTip = false

var axios = require('axios')
Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
