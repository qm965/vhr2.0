import request from '@/utils/request.js'
const api='/api/personnel/ec'
export const ecRecords=()=>request({url:api,method:'get'})
export const addEc=data=>request({url:api,method:'post',data})
export const updateEc=data=>request({url:api,method:'put',data})
export const deleteEc=id=>request({url:`${api}/${id}`,method:'delete'})
