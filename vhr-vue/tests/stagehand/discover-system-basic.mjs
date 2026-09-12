import {config} from 'dotenv'
import {Stagehand} from '@browserbasehq/stagehand'
import {z} from 'zod'

config({path: '.env.stagehand'})

const required = ['DEEPSEEK_API_KEY', 'VHR_STAGEHAND_USERNAME', 'VHR_STAGEHAND_PASSWORD']
const missing = required.filter((name) => !process.env[name])
if (missing.length) throw new Error(`缺少 ${missing.join('、')}，请填写 .env.stagehand。`)

const stagehand = new Stagehand({
  env: 'LOCAL',
  selfHeal: true,
  model: {modelName: process.env.VHR_STAGEHAND_MODEL || 'deepseek/deepseek-flash', apiKey: process.env.DEEPSEEK_API_KEY},
  localBrowserLaunchOptions: {headless: process.env.VHR_STAGEHAND_HEADLESS === 'true'},
})

try {
  await stagehand.init()
  const [page] = stagehand.context.pages()
  await page.goto(process.env.VHR_BASE_URL || 'http://127.0.0.1:5173')
  await page.waitForSelector('input[placeholder="请输入用户名..."]')
  const loggedIn = await page.evaluate(({username, password}) => {
    const fill = (selector, value) => {
      const input = document.querySelector(selector)
      const setter = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value')?.set
      setter?.call(input, value)
      input.dispatchEvent(new Event('input', {bubbles: true}))
      input.dispatchEvent(new Event('change', {bubbles: true}))
    }
    fill('input[placeholder="请输入用户名..."]', username)
    fill('input[placeholder="请输入用户密码..."]', password)
    ;[...document.querySelectorAll('button')].find((button) => button.textContent?.trim() === '登录')?.click()
    return true
  }, {username: process.env.VHR_STAGEHAND_USERNAME, password: process.env.VHR_STAGEHAND_PASSWORD})
  if (!loggedIn) throw new Error('未找到登录表单。')
  await page.waitForTimeout(1000)
  const sessionCreated = await page.evaluate(() => Boolean(window.sessionStorage.getItem('hr')))
  if (!sessionCreated) throw new Error('登录未建立会话，请检查 .env.stagehand 中的本地测试账号和后端响应。')
  const openSystemMenu = await page.evaluate(() => {
    const item = [...document.querySelectorAll('.el-sub-menu__title, .el-menu-item')]
      .find((element) => element.textContent?.trim() === '系统管理')
    item?.click()
    return Boolean(item)
  })
  if (!openSystemMenu) throw new Error('未找到“系统管理”菜单。')
  await page.waitForTimeout(300)
  const openBasicSettings = await page.evaluate(() => {
    const item = [...document.querySelectorAll('.el-sub-menu__title, .el-menu-item')]
      .find((element) => element.textContent?.trim() === '基础信息设置')
    item?.click()
    return Boolean(item)
  })
  if (!openBasicSettings) throw new Error('未找到“基础信息设置”菜单。')
  await page.waitForTimeout(500)
  const discovery = await stagehand.extract(
    '只读取当前“基础信息设置”页面。列出可见的标签页、每个标签页已展示的字段或操作，以及发现的缺失功能。禁止点击任何新增、编辑、删除、保存或提交按钮。',
    z.object({tabs: z.array(z.string()), visibleCapabilities: z.array(z.string()), missingOrUnavailable: z.array(z.string())}),
  )
  console.log(JSON.stringify(discovery, null, 2))
} finally {
  await stagehand.close()
}
