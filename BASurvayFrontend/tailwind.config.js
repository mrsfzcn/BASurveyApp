/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        firstColor: "#212a3e",
        secondColor: "#64e9b1",
      },
    },
  },
  plugins: [],
};
