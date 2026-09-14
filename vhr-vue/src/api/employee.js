import request from '@/utils/request.js'
export const employees = params => request({url: '/api/employee/basic/', method: 'get', params})
export const addEmployee = data => request({url: '/api/employee/basic/', method: 'post', data})
export const updateEmployee = data => request({url: '/api/employee/basic/', method: 'put', data})
export const deleteEmployee = id => request({url: `/api/employee/basic/${id}`, method: 'delete'})
export const employeeOptions = path => request({url: `/api/employee/basic/${path}`, method: 'get'})
export const importEmployees = data => request({url:'/api/employee/basic/import',method:'post',data,headers:{'Content-Type':'multipart/form-data'}})
