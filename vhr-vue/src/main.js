import {createApp} from 'vue'
import {createPinia} from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

import App from './App.vue'
import router from './router'
import {menusStore} from '@/stores/index.js'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {locale: zhCn})

const menuStore = menusStore()

router.beforeEach(async (to) => {
    if (to.path === '/') {
        return true
    }
    if (!window.sessionStorage.getItem('hr')) {
        return {path: '/', query: {redirect: to.fullPath}}
    }
    if (menuStore.menus.length > 0) {
        return true
    }
    try {
        const routes = await menuStore.initMenus()
        routes.forEach(route => router.addRoute(route))
        return to.fullPath
    } catch {
        menuStore.clearMenus()
        window.sessionStorage.removeItem('hr')
        return {path: '/', query: {redirect: to.fullPath}}
    }
})

app.mount('#app')
