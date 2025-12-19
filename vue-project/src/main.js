import { createApp } from 'vue'
import App from './App.vue'
//引入路由
import router from './router'
//引入ElementPlus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//引入Element图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
//引入全局样式
import '@/assets/global.css'
import { createPinia } from 'pinia' // 引入 createPinia

const app=createApp(App)

app.use(createPinia())
app.use(router)

app.use(ElementPlus)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
