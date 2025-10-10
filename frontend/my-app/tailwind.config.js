/** @type {import('tailwindcss').Config} */
export default {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      animation: {
        'pulse-banner': 'pulse 1.3s ease-in-out infinite alternate',
      },
    },
  },
  plugins: [],
}
