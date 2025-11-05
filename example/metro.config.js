const {getDefaultConfig, mergeConfig} = require('@react-native/metro-config');

const path = require('path');
const pack = require('../package.json');
const escape = require('escape-string-regexp');
const exclusionList = require('metro-config/src/defaults/exclusionList');

const exampleRoot = __dirname;
const exampleNodeModules = path.join(exampleRoot, 'node_modules');

const libraryRoot = path.resolve(__dirname, '..');
const libraryNodeModules = path.join(libraryRoot, 'node_modules');

const modulesPeer = Object.keys({...pack.peerDependencies});
const modulesBlackList = modulesPeer.map(m => {
  return new RegExp(`^${escape(path.join(libraryNodeModules, m))}\\/.*$`);
});
const modules = modulesPeer.reduce(
  (prev, current) => {
    prev[current] = path.join(exampleNodeModules, current);
    return prev;
  },
  {[pack.name]: libraryRoot},
);

/**
 * Metro configuration
 * https://reactnative.dev/docs/metro
 *
 * @type {import('@react-native/metro-config').MetroConfig}
 */
const config = {
  projectRoot: exampleRoot,
  watchFolders: [libraryRoot],
  resolver: {
    blacklistRE: exclusionList(modulesBlackList),
    extraNodeModules: modules,
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
