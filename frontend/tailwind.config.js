/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#fff1f3',
          100: '#ffe0e5',
          200: '#ffc7d0',
          300: '#ffa3b3',
          400: '#fe6d88',
          500: '#FE2C55',
          600: '#e6194a',
          700: '#c2103d',
          800: '#a1103a',
          900: '#8a1237',
        },
      }
    },
  },
  plugins: [],
}
