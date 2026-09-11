/*在这个文件中我们对请求和响应进行封装*/
import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

const service = axios.create({
    timeout: 10000
})

//这个是请求拦截器，如果是使用 JWT 或者其他令牌登录的话，那么可以在请求拦截器中统一添加令牌
service.interceptors.request.use(config => config);
service.interceptors.response.use(response => {
    //获取服务端返回的状态码，如果服务端没有设置状态码，默认就是 200
    const code = response.data?.status ?? response.status;
    if (code === 200) {
        //说明请求成功
        if (response.data?.message) {
            ElMessage.success(response.data.message)
        }
        //返回服务端返回的 JSON
        return response.data;
    }
    const message = response.data?.message ?? '请求失败';
    ElMessage.error(message)
    return Promise.reject(new Error(message));
}, error => {
    if (error.response?.status === 401) {
        //说明未登录
        window.sessionStorage.removeItem('hr');
        router.replace({path: '/', query: {redirect: router.currentRoute.value.fullPath}});
    }
    //HTTP 状态码不是 200，就会进入到这个回调中
    ElMessage.error(error.response?.data?.message ?? '网络请求失败');
    return Promise.reject(error);
})

export default service;
