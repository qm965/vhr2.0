import request from '@/utils/request.js'
export const roles=()=>request({url:'/api/system/basic/permiss/',method:'get'})
export const menus=()=>request({url:'/api/system/basic/permiss/menus',method:'get'})
export const menuIds=rid=>request({url:`/api/system/basic/permiss/mids/${rid}`,method:'get'})
export const savePermissions=(rid,mids)=>request({url:'/api/system/basic/permiss/',method:'put',params:{rid,mids}})
export const addRole=data=>request({url:'/api/system/basic/permiss/role',method:'post',data})
export const deleteRole=rid=>request({url:`/api/system/basic/permiss/role/${rid}`,method:'delete'})
