import request from '@/utils/request.js'
export const profile=()=>request({url:'/api/hr/info',method:'get'})
export const updateProfile=data=>request({url:'/api/hr/info',method:'put',data})
export const updatePassword=data=>request({url:'/api/hr/pass',method:'put',data})
