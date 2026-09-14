import request from '@/utils/request.js'
export const chatUsers=()=>request({url:'/api/chat/hrs',method:'get'})
