import {ref, computed} from 'vue'
import {defineStore} from 'pinia'
import {loadMenus} from "@/api/menus.js";

const modules = import.meta.glob([
    '@/views/**/*.vue',
    '!@/views/HomeView.vue',
    '!@/views/LoginView.vue',
]);
const homeView = () => import('@/views/HomeView.vue');
export const useCounterStore = defineStore('counter', () => {
    const count = ref(0)
    const doubleCount = computed(() => count.value * 2)

    function increment() {
        count.value++
    }

    return {count, doubleCount, increment}
})
export const menusStore = defineStore('menus', {
    state: () => (
        {
            //这个变量就用来保存服务端返回的菜单数据，每一次页面跳转的时候，如果 menus 中有值，说明就是普通的页面点击跳转，如果这个 menus 为空，说明可能是用户按 F5 进行页面跳转的
            menus: []
        }
    ),
    actions: {
        clearMenus() {
            this.menus = [];
        },
        async initMenus() {
            const response = await loadMenus();
            const menus = Array.isArray(response.data) ? response.data : [];
            this.menus = menus;
            return formatMenus(menus);
        },
    }
})

function formatMenus(menus) {
    return menus.map(menu => {
        let {path, name, children, component} = menu;
        if (Array.isArray(children)) {
            //递归去格式化 children
            children = formatMenus(children);
        }
        return {
            path: path,
            name: name,
            children: children,
            component: loadView(component)
        }
    });
}

function loadView(viewPath) {
    if (viewPath === '/src/views/HomeView.vue') {
        return homeView;
    }
    const component = modules[viewPath];
    if (!component) {
        throw new Error(`未找到菜单组件: ${viewPath}`);
    }
    return component;
}
