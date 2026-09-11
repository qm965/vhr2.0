<template>
  <div>
    <el-container>
      <el-header style="background-color: #02adff;align-items: center;display: flex;justify-content: space-between">
        <div style="font-family: 华文行楷;font-size: 30px">微人事</div>
        <div>
          <el-dropdown style="cursor:pointer;" @command="menuHandle">
            <span class="el-dropdown-link" style="display: flex;align-items: center">
              {{ hr.name }}
              <img :src="hr.userface" style="width: 48px;height: 48px;border-radius: 24px" alt="">
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="usercenter">个人中心</el-dropdown-item>
                <el-dropdown-item command="settings">设置</el-dropdown-item>
                <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu router>
            <template v-for="(menu, indexi) in menus" :key="menu.path ?? indexi">
              <el-sub-menu v-if="!menu.hidden" :index="String(indexi)">
                <template #title>
                  <el-icon><Avatar /></el-icon>
                  <span>{{ menu.name }}</span>
                </template>
                <el-menu-item v-for="(child, indexj) in menu.children" :key="child.path ?? indexj" :index="child.path">
                  {{ child.name }}
                </el-menu-item>
              </el-sub-menu>
            </template>
          </el-menu>
        </el-aside>
        <el-main>
          <el-breadcrumb v-if="route.path !== '/home'" :separator-icon="ArrowRight">
            <el-breadcrumb-item :to="{path: '/home'}">主页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ route.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div v-if="route.path === '/home'" style="font-size: 35px;font-family: 华文行楷;color: red;text-align: center">
            欢迎来到微人事脚手架！
          </div>
          <RouterView />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import {computed} from 'vue'
import {storeToRefs} from 'pinia'
import {RouterView, useRoute, useRouter} from 'vue-router'
import {ArrowRight, Avatar} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {logout} from '@/api/login.js'
import {menusStore} from '@/stores/index.js'

const router = useRouter()
const route = useRoute()
const menuStore = menusStore()
const {menus} = storeToRefs(menuStore)
const hr = computed(() => JSON.parse(window.sessionStorage.getItem('hr') ?? '{}'))

async function menuHandle(command) {
  if (command !== 'logout') {
    return
  }
  try {
    await ElMessageBox.confirm('此操作将注销登录，是否继续?', '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    })
    await logout()
    window.sessionStorage.removeItem('hr')
    menuStore.clearMenus()
    await router.replace('/')
  } catch (error) {
    if (error === 'cancel' || error === 'close') {
      ElMessage.info('已取消操作')
    }
  }
}
</script>

<style scoped>
</style>
