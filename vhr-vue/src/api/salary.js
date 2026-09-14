import request from '@/utils/request.js'
export const salaries = () => request({url: '/api/salary/sob/', method: 'get'})
export const addSalary = data => request({url: '/api/salary/sob/', method: 'post', data})
export const updateSalary = data => request({url: '/api/salary/sob/', method: 'put', data})
export const deleteSalary = id => request({url: `/api/salary/sob/${id}`, method: 'delete'})
