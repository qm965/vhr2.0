import {expect, test} from '@playwright/test'

const enabled = process.env.VHR_E2E_ALLOW_WRITE === 'true'
const username = process.env.VHR_E2E_USERNAME
const password = process.env.VHR_E2E_PASSWORD

test.describe('系统基础信息设置', () => {
  test.skip(!enabled || !username || !password, '仅允许在独立测试库中执行写操作。')
  test('管理员可维护部门、职位与职称', async ({page}) => {
    const stamp = Date.now()
    const positionName = `自动化测试职位-${stamp}`
    const departmentName = `自动化测试部门-${stamp}`
    const targetDepartmentName = `自动化目标部门-${stamp}`
    const movedDepartmentName = `${departmentName}-已移动`
    const joblevelName = `自动化测试职称-${stamp}`
    await page.goto('/')
    await page.getByPlaceholder('请输入用户名...').fill(username)
    await page.getByPlaceholder('请输入用户密码...').fill(password)
    await page.getByRole('button', {name: '登录'}).click()
    await page.getByText('系统管理', {exact: true}).click()
    await page.getByText('基础信息设置', {exact: true}).click()

    await expect(page.getByRole('tab', {name: '部门管理'})).toBeVisible()
    // 初始部门树来自当前开发测试库，根节点名称并不固定；取第一个可见根节点即可覆盖新增下级部门链路。
    const rootContent = page.locator('.el-tree > .el-tree-node > .el-tree-node__content').first()
    await rootContent.getByRole('button', {name: '添加下级'}).click()
    const departmentDialog = page.locator('.el-dialog').filter({hasText: '添加下级部门'})
    await departmentDialog.locator('input').fill(targetDepartmentName)
    await departmentDialog.getByRole('button', {name: '确认'}).click()
    await expect(page.getByText(targetDepartmentName, {exact: true})).toBeVisible()

    await rootContent.getByRole('button', {name: '添加下级'}).click()
    await departmentDialog.locator('input').fill(departmentName)
    await departmentDialog.getByRole('button', {name: '确认'}).click()
    await expect(page.getByText(departmentName, {exact: true})).toBeVisible()
    await page.getByRole('button', {name: `编辑部门${departmentName}`}).click()
    const editDialog = page.locator('.el-dialog').filter({hasText: '编辑部门'})
    await editDialog.locator('input').first().fill(movedDepartmentName)
    await editDialog.getByPlaceholder('请选择上级部门').click()
    await page.getByRole('option', {name: new RegExp(`${targetDepartmentName}$`)}).click()
    await editDialog.getByRole('button', {name: '保存'}).click()
    await expect(page.getByText(movedDepartmentName, {exact: true})).toBeVisible()

    await page.getByRole('tab', {name: '职位管理'}).click()
    await page.getByPlaceholder('请输入职位名称').fill(positionName)
    await page.getByRole('button', {name: '添加'}).click()
    await expect(page.getByText(positionName, {exact: true})).toBeVisible()

    await page.getByRole('tab', {name: '职称管理'}).click()
    await page.getByRole('button', {name: '添加职称'}).click()
    const joblevelDialog = page.locator('.el-dialog').filter({hasText: '添加职称'})
    await joblevelDialog.getByRole('textbox', {name: /职称名称/}).fill(joblevelName)
    await joblevelDialog.getByRole('button', {name: '保存'}).click()
    await expect(page.getByText(joblevelName, {exact: true})).toBeVisible()
  })
})
