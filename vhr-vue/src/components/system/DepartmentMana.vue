<template>
  <section class="department-manager" v-loading="loading">
    <el-input v-model="keyword" clearable placeholder="按部门名称筛选" aria-label="按部门名称筛选" />
    <el-tree class="tree" :data="filteredDepartments" node-key="id" default-expand-all :expand-on-click-node="false">
      <template #default="{ data }">
        <span class="node">
          <span>{{ data.name }}</span>
          <span>
            <el-button link type="primary" @click="openAdd(data)">添加下级</el-button>
            <el-button link type="danger" :disabled="data.isParent" @click="confirmDelete(data)">删除</el-button>
          </span>
        </span>
      </template>
    </el-tree>
    <el-dialog v-model="dialogVisible" title="添加下级部门" width="420px" destroy-on-close>
      <el-form label-width="90px" @submit.prevent="submit">
        <el-form-item label="上级部门"><span>{{ parent?.name }}</span></el-form-item>
        <el-form-item label="部门名称" required><el-input v-model="form.name" maxlength="32" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitting" @click="submit">确认</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {addDepartment, deleteDepartment, loadDepartmentTree} from '@/api/system/department.js'

const departments = ref([])
const keyword = ref('')
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const parent = ref(null)
const form = ref({name: ''})

const filteredDepartments = computed(() => filterTree(departments.value, keyword.value.trim()))

function filterTree(nodes, term) {
  if (!term) return nodes
  return nodes.reduce((result, node) => {
    const children = filterTree(node.children || [], term)
    if (node.name.includes(term) || children.length) result.push({...node, children})
    return result
  }, [])
}
async function reload() {
  loading.value = true
  try { departments.value = (await loadDepartmentTree()).data || [] } finally { loading.value = false }
}
function openAdd(node) {
  parent.value = node
  form.value = {name: ''}
  dialogVisible.value = true
}
async function submit() {
  if (!form.value.name.trim()) return ElMessage.warning('请输入部门名称')
  submitting.value = true
  try {
    await addDepartment({name: form.value.name, parentId: parent.value.id})
    dialogVisible.value = false
    await reload()
  } finally { submitting.value = false }
}
async function confirmDelete(node) {
  try {
    await ElMessageBox.confirm(`将永久删除部门“${node.name}”，是否继续？`, '删除确认', {type: 'warning'})
    await deleteDepartment(node.id)
    await reload()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') ElMessage.error(error.message || '删除部门失败')
  }
}
onMounted(reload)
</script>

<style scoped>
.department-manager { max-width: 760px; }
.tree { margin-top: 16px; }
.node { width: 100%; display: flex; justify-content: space-between; align-items: center; padding-right: 12px; }
</style>
