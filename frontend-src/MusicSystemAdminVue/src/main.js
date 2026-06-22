import Vue from 'vue'
import App from './App'
import router from './router/index'
import store from './store/index'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/main.css'
import 'babel-polyfill'
import '@/../config/initialize'
import './assets/font2/iconfont.css'
import VCharts from 'v-charts'

Vue.use(ElementUI)

Vue.use(VCharts)

document.title = '闊充箰鍚庡彴绠＄悊'

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
