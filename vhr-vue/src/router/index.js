import {createRouter, createWebHistory} from 'vue-router'
import LoginView from "@/views/LoginView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    /*这个地方就定义了地址和页面之间的映射关系
    * 即如果浏览器地址栏是 / 则展示 HomeView 这个页面
    * 如果浏览器地址栏是 /about，则展示 AboutView 这个页面
    * */
    routes: [
        {
            path: '/',
            name: '登录',
            component: LoginView,
            hidden: true
        },
        {
            path: '/home',
            name: '首页',
            component: () => import('@/views/HomeView.vue'),
            children: [
                {
                    path: '/list',
                    name: '用户列表',
                    component: () => import('@/views/user/UserList.vue')
                },
                {
                    path: '/analysis',
                    name: '用户分析',
                    component: () => import('@/views/user/UserAnalysis.vue')
                },
                {
                    path: '/log',
                    name: '用户日志',
                    component: () => import('@/views/user/UserLog.vue')
                }
            ]
        },
        {
            path: '/about',
            name: 'about',
            hidden: true,
            // route level code-splitting
            // this generates a separate chunk (About.[hash].js) for this route
            // which is lazy-loaded when the route is visited.
            //相对于上面的 HomeView，这里的 AboutView 是一种懒加载，即在需要的时候才去加载页面
            component: () => import('../views/AboutView.vue')
        },
        {
            path: '/hrinfo', name: '个人中心', component: () => import('@/views/HrInfo.vue')
        }
    ]
})

export default router
