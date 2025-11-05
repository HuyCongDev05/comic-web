// bật lại khi không cần https nữa
// import { defineConfig } from "vite";
// import react from "@vitejs/plugin-react";
//
// // https://vite.dev/config/
// export default defineConfig({
//   plugins: [react()],
// });

import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import fs from "fs";

export default defineConfig({
    plugins: [react()],
    server: {
        https: {
            key: fs.readFileSync("./localhost-key.pem"),
            cert: fs.readFileSync("./localhost.pem"),
        },
        port: 5173,
    },
});

