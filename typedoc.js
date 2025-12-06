/** @type {Partial<import("typedoc").TypeDocOptions>} */
module.exports = {
  name: 'React Native Location',
  out: 'docs',
  tsconfig: 'tsconfig.json',
  githubPages: true,

  entryPoints: ['src/index.ts'],
  projectDocuments: ['README.md', 'instructions/*.md'],
  highlightLanguages: ['bash', 'typescript', 'javascript', 'json', 'xml'],

  navigationLinks: {
    Github: 'https://github.com/hyoper/react-native-location',
    Npm: 'https://www.npmjs.com/package/@hyoper/rn-location',
  },
  customCss: ['typedoc.css'],
};
