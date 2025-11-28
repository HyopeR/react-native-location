/** @type {Partial<import("typedoc").TypeDocOptions>} */
module.exports = {
  name: "React Native Location",
  entryPoints: ["./src/index.ts"],
  out: "docs",
  tsconfig: "./tsconfig.json",
  githubPages: true,
  navigationLinks: {
    Github: "https://github.com/hyoper/react-native-location"
  },
  customCss: ['./typedoc.css'],
};
