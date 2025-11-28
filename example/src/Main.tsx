import React, {useMemo, useState} from 'react';
import {List} from './List';
import {
  HomePage,
  CurrentPage,
  ForegroundPage,
  BackgroundPage,
  ManagerPage,
  PermissionPage,
  PageSection,
} from './pages';

const Pages: PageSection[] = [
  {
    title: 'Foundation Usages',
    data: [
      {title: 'Current Location - Get', name: 'Current', section: 0},
      {title: 'Foreground Location - Tracking', name: 'Foreground', section: 0},
      {title: 'Background Location - Tracking', name: 'Background', section: 0},
    ],
  },
  {
    title: 'Helper Usages',
    data: [
      {title: 'Gps Manager - Helper', name: 'Manager', section: 1},
      {title: 'Permission Manager - Helper', name: 'Permission', section: 1},
    ],
  },
];

export const Main = () => {
  const [name, setName] = useState('Home');

  const Page = useMemo(() => {
    switch (name) {
      case 'Current':
        return CurrentPage;
      case 'Foreground':
        return ForegroundPage;
      case 'Background':
        return BackgroundPage;
      case 'Manager':
        return ManagerPage;
      case 'Permission':
        return PermissionPage;
      default:
        return null;
    }
  }, [name]);

  if (Page) {
    return <Page back={() => setName('Home')} />;
  }

  return (
    <HomePage>
      <List sections={Pages} set={setName} />
    </HomePage>
  );
};
