import {defineConfig} from "vite";
import react from "@vitejs/plugin-react";
import path from "path";
import {fileURLToPath} from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export default defineConfig({
  plugins: [react()],
    resolve: {
        alias: {
            "@comics/shared": path.resolve(__dirname, "../shared"),
        },
    },
  server: {
    port: 5000,
    strictPort: true,
      fs: {
          allow: [".."],
      },

      // Mở host để có thể truy cập từ bên ngoài (qua tunnel)
      // host: "0.0.0.0",
      // proxy: {
      //   "/api": {
      //     target: "http://localhost:8080",
      //     changeOrigin: true,
      //   },
      // },
  },
});
