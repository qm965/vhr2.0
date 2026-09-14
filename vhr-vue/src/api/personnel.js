import request from '@/utils/request.js'
const api='/api/personnel/ec'
export const ecRecords=()=>request({url:api,method:'get'})
export const addEc=data=>request({url:api,method:'post',data})
export const updateEc=data=>request({url:api,method:'put',data})
export const deleteEc=id=>request({url:`${api}/${id}`,method:'delete'})
const train='/api/personnel/train'
export const trainRecords=()=>request({url:train,method:'get'})
export const addTrain=data=>request({url:train,method:'post',data})
export const updateTrain=data=>request({url:train,method:'put',data})
export const deleteTrain=id=>request({url:`${train}/${id}`,method:'delete'})
const move='/api/personnel/remove';export const moveRecords=()=>request({url:move,method:'get'});export const addMove=data=>request({url:move,method:'post',data});export const updateMove=data=>request({url:move,method:'put',data});export const deleteMove=id=>request({url:`${move}/${id}`,method:'delete'})
