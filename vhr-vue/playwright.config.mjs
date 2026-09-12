import {defineConfig} from '@playwright/test'
import {config} from 'dotenv'

config({path: '.env.e2e'})

export default defineConfig({
  testDir: './tests/e2e',
  timeout: 30_000,
  use: {baseURL: process.env.VHR_E2E_BASE_URL || 'http://127.0.0.1:5174', trace: 'retain-on-failure'},
  reporter: [['list'], ['html', {open: 'never'}]],
  webServer: process.env.VITE_API_TARGET ? {
    command: 'npm run dev -- --mode e2e --port 5174',
    url: 'http://127.0.0.1:5174',
    reuseExistingServer: !process.env.CI,
  } : undefined,
})
