const Description = {
  foreground: 'Required to use location information while the app is in use.',
  background:
    'Required to use location information while the app is open in the background.',
};

const Android = {
  /**
   * @param {any[]} permissions
   * @param {string} permission
   */
  addPermission: (permissions, permission) => {
    const exist = permissions.some(p => p.$['android:name'] === permission);
    if (!exist) {
      permissions.push({$: {'android:name': permission}});
    }
  },

  /**
   * @param {any[]} services
   * @param {Record<string, any>} service
   */
  addService: (services, service) => {
    const name = service['android:name'];
    const exists = services.some(s => s.$['android:name'] === name);
    if (!exists) {
      services.push({$: service});
    }
  },
};

const Ios = {
  /**
   * @param {Record<string, string>} info
   * @param {string} key
   * @param {string} value
   */
  addPermission: (info, key, value) => {
    if (!info.hasOwnProperty(key)) {
      info[key] = value;
    }
  },

  /**
   * @param {any[]} modes
   * @param {string} mode
   */
  addBackgroundMode: (modes, mode) => {
    if (!modes.includes(mode)) {
      modes.push(mode);
    }
  },
};

module.exports = {Description, Android, Ios};
