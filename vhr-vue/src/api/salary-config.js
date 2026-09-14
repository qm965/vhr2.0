import request from '@/utils/request.js'
export const assignments=()=>request({url:'/api/salary/sobcfg/',method:'get'})
export const salaryOptions=()=>request({url:'/api/salary/sobcfg/salaries',method:'get'})
export const assignSalary=(eid,sid)=>request({url:'/api/salary/sobcfg/',method:'put',params:{eid,sid}})
