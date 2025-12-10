/**
 * @type {Partial<import("typedoc").TypeDocOptions>}
 */
module.exports = {
  name: 'React Native Location',
  out: 'docs',
  tsconfig: 'tsconfig.json',
  githubPages: true,
  favicon: 'preview.ico',

  defaultCategory: 'Package',
  entryPoints: ['src/index.ts'],
  plugin: ['./typedoc-front-matter.mjs'],
  projectDocuments: ['README.md', 'instructions/*.md'],
  projectDetails: {
    README: {
      title: 'Home',
      group: 'Documents',
      category: 'Instructions',
    },
    GUIDELINES: {
      title: 'Guidelines',
      group: 'Documents',
      category: 'Instructions',
    },
    HELPERS: {
      title: 'Helpers',
      group: 'Documents',
      category: 'Instructions',
    },
    INSTALLATION: {
      title: 'Installation',
      group: 'Documents',
      category: 'Instructions',
    },
  },

  navigationLinks: {
    Github: 'https://github.com/hyoper/react-native-location',
    Npm: 'https://www.npmjs.com/package/@hyoper/rn-location',
  },

  highlightLanguages: ['bash', 'typescript', 'json', 'json5', 'xml'],
  customCss: ['typedoc-custom.css'],
  customJs: ['typedoc-custom.js'],
};
