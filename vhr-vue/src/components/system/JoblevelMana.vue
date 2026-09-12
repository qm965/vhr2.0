<template>
  <section v-loading="loading">
    <el-form :inline="true" @submit.prevent="openCreate">
      <el-form-item><el-button type="primary" @click="openCreate">添加职称</el-button></el-form-item>
    </el-form>
    <el-table :data="records" border stripe>
      <el-table-column prop="id" label="编号" width="90" />
      <el-table-column prop="name" label="职称名称" />
      <el-table-column prop="titleLevel" label="职称级别" />
      <el-table-column prop="createDate" label="创建时间" />
      <el-table-column label="启用状态" width="120"><template #default="{row}"><el-tag :type="row.enabled ? 'success' : 'info'">{{ row.enabled ? '已启用' : '已停用' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="180"><template #default="{row}"><el-button link type="primary" @click="openEdit(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" v-model:page-size="size" class="pagination" background layout="total, sizes, prev, pager, next" :page-sizes="[10, 20, 50]" :total="total" @change="reload" />
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑职称' : '添加职称'" width="440px" destroy-on-close>
      <el-form label-width="90px" @submit.prevent="save">
        <el-form-item label="职称名称" required><el-input v-model="form.name" maxlength="32" /></el-form-item>
        <el-form-item label="职称级别" required><el-select v-model="form.titleLevel"><el-option v-for="level in levels" :key="level" :label="level" :value="level" /></el-select></el-form-item>
        <el-form-item label="是否启用"><el-switch v-model="form.enabled" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitting" @click="save">保存</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {addJoblevel, deleteJoblevel, loadJoblevels, updateJoblevel} from '@/api/system/joblevel.js'

const levels = ['正高级', '副高级', '中级', '初级', '员级']
const records = ref([]), total = ref(0), page = ref(1), size = ref(10), loading = ref(false), submitting = ref(false), dialogVisible = ref(false)
const form = ref({})
async function reload() { loading.value = true; try { const response = await loadJoblevels({page: page.value, size: size.value}); records.value = response.data || []; total.value = response.total || 0 } finally { loading.value = false } }
function openCreate() { form.value = {name: '', titleLevel: '初级', enabled: true}; dialogVisible.value = true }
function openEdit(row) { form.value = {...row}; dialogVisible.value = true }
async function save() { if (!form.value.name?.trim()) return ElMessage.warning('请输入职称名称'); submitting.value = true; try { if (form.value.id) await updateJoblevel(form.value); else await addJoblevel(form.value); dialogVisible.value = false; await reload() } finally { submitting.value = false } }
async function remove(row) { try { await ElMessageBox.confirm(`将永久删除职称“${row.name}”，是否继续？`, '删除确认', {type: 'warning'}); await deleteJoblevel(row.id); await reload() } catch (error) { if (error !== 'cancel' && error !== 'close') ElMessage.error(error.message || '删除职称失败') } }
onMounted(reload)
</script>

<style scoped>.pagination { justify-content: flex-end; margin-top: 16px; }</style>
