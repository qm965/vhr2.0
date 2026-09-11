import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    // 固定监听本机 IPv4 回环地址，便于通过 127.0.0.1 访问，且不暴露到局域网。
    host: '127.0.0.1',
    proxy: {
      // 例如：http://127.0.0.1:5173/api/bar -> http://localhost:8080/bar
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      // 代理 WebSocket 或 Socket.IO：ws://127.0.0.1:5173/socket.io -> ws://localhost:5174/socket.io
      '/socket.io': {
        target: 'ws://localhost:5174',
        ws: true,
      },
    },
  },
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
