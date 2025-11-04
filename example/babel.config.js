const path = require('path');
const pack = require('../package.json');

const librarySource = path.resolve(__dirname, '..', pack.source);

module.exports = {
  presets: ['module:@react-native/babel-preset'],
  plugins: [
    [
      'module-resolver',
      {
        extensions: ['.tsx', '.ts', 'jsx', '.js', '.json'],
        alias: {
          [pack.name]: librarySource,
        },
      },
    ],
  ],
};
