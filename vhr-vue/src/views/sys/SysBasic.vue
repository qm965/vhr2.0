<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="部门管理" name="department"><department-mana /></el-tab-pane>
      <el-tab-pane label="职位管理" name="position"><position-mana /></el-tab-pane>
      <el-tab-pane label="职称管理" name="joblevel"><joblevel-mana /></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>

import PositionMana from "@/components/system/PositionMana.vue";
import DepartmentMana from '@/components/system/DepartmentMana.vue'
import JoblevelMana from '@/components/system/JoblevelMana.vue'

import {ref} from 'vue'
const activeName = ref('department')

</script>

<style scoped>

</style>
