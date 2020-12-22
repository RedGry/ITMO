import Vue from 'vue'
import App from './App.vue'
import router from "@/router";
import Notifications from 'vue-notification';
import Axios from "axios";

Vue.config.productionTip = false
Vue.use(Notifications)
Vue.prototype.$axios = Axios.create({baseUrl: '/api'});

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
