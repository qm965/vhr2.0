import request from '@/utils/request.js'
export const hrs = keywords => request({url:'/api/system/hr/',method:'get',params:{keywords}})
export const roles = () => request({url:'/api/system/hr/roles',method:'get'})
export const updateHr = data => request({url:'/api/system/hr/',method:'put',data})
export const updateRoles = (hrid,rids) => request({url:'/api/system/hr/role',method:'put',params:{hrid,rids}})
export const deleteHr = id => request({url:`/api/system/hr/${id}`,method:'delete'})
