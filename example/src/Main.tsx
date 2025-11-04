import React, {useMemo, useState} from 'react';
import {List} from './List';
import {HomePage, ForegroundPage, BackgroundPage, PageSection} from './pages';

const Pages: PageSection[] = [
  {
    title: 'Foundation Usages',
    data: [
      {title: 'Foreground Usage', name: 'Foreground', section: 0},
      {title: 'Background Usage', name: 'Background', section: 0},
    ],
  },
];

export const Main = () => {
  const [name, setName] = useState('Home');

  const Page = useMemo(() => {
    switch (name) {
      case 'Foreground':
        return ForegroundPage;
      case 'Background':
        return BackgroundPage;
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
