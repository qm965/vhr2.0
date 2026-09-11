import { config } from 'dotenv';
import { Stagehand } from '@browserbasehq/stagehand';
import { z } from 'zod';

config({ path: '.env.stagehand' });

const required = ['DEEPSEEK_API_KEY', 'VHR_STAGEHAND_USERNAME', 'VHR_STAGEHAND_PASSWORD'];
const missing = required.filter((name) => !process.env[name]);
if (missing.length > 0) {
  throw new Error(`Missing ${missing.join(', ')}. Copy stagehand.env.example to .env.stagehand and fill in a disposable local account.`);
}

const stagehand = new Stagehand({
  env: 'LOCAL',
  selfHeal: true,
  model: {
    modelName: process.env.VHR_STAGEHAND_MODEL || 'deepseek/deepseek-flash',
    apiKey: process.env.DEEPSEEK_API_KEY,
  },
  localBrowserLaunchOptions: {
    headless: process.env.VHR_STAGEHAND_HEADLESS === 'true',
  },
});

try {
  await stagehand.init();
  const [page] = stagehand.context.pages();
  await page.goto(process.env.VHR_BASE_URL || 'http://127.0.0.1:5173');
  await page.waitForSelector('input[placeholder="请输入用户名..."]');

  const loginPrepared = await page.evaluate(({ username, password }) => {
    const setValue = (input, value) => {
      const setter = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value')?.set;
      setter?.call(input, value);
      input.dispatchEvent(new Event('input', { bubbles: true }));
      input.dispatchEvent(new Event('change', { bubbles: true }));
    };
    const usernameInput = document.querySelector('input[placeholder="请输入用户名..."]');
    const passwordInput = document.querySelector('input[placeholder="请输入用户密码..."]');
    const loginButton = [...document.querySelectorAll('button')]
      .find((button) => button.textContent?.trim() === '登录');

    if (!usernameInput || !passwordInput || !loginButton) {
      return false;
    }
    setValue(usernameInput, username);
    setValue(passwordInput, password);
    loginButton.click();
    return true;
  }, {
    username: process.env.VHR_STAGEHAND_USERNAME,
    password: process.env.VHR_STAGEHAND_PASSWORD,
  });

  if (!loginPrepared) {
    throw new Error('Could not find the vhr login form.');
  }
  await page.waitForTimeout(1000);

  const loggedIn = await page.evaluate(() => Boolean(window.sessionStorage.getItem('hr')));
  if (!loggedIn) {
    throw new Error('Login did not create a session. Check the local test account and backend response.');
  }

  const discovery = await stagehand.extract(
    'List every visible first-level menu. For each one, list the visible child menus and describe the business responsibility implied by its label. Do not click controls that create, edit, delete, or submit business data.',
    z.object({
      currentPage: z.string(),
      menus: z.array(z.object({
        label: z.string(),
        children: z.array(z.string()),
        inferredResponsibility: z.string(),
      })),
    }),
  );

  console.log(JSON.stringify(discovery, null, 2));
} finally {
  await stagehand.close();
}
