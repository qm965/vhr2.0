import request from '@/utils/request.js'

export function loadJoblevels(params) {
  return request.get('/api/system/basic/joblevels', {params})
}

export function addJoblevel(data) {
  return request.post('/api/system/basic/joblevels', data)
}

export function updateJoblevel(data) {
  return request.put('/api/system/basic/joblevels', data)
}

export function deleteJoblevel(id) {
  return request.delete(`/api/system/basic/joblevels/${id}`)
}
