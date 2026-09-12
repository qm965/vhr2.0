import request from '@/utils/request.js'

export function loadDepartmentTree() {
  return request.get('/api/system/basic/departments')
}

export function addDepartment(data) {
  return request.post('/api/system/basic/departments', data)
}

export function deleteDepartment(id) {
  return request.delete(`/api/system/basic/departments/${id}`)
}
