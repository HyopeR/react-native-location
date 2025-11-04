import {ReactNode} from 'react';

export type PageProps = {
  back?: () => void;
  children?: ReactNode;
};

export type PageSection = {
  title: string;
  data: PageItem[];
};

export type PageItem = {
  name: string;
  title: string;
  section: number;
};
