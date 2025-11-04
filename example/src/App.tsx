import React from 'react';
import {StatusBar} from 'react-native';
import {
  SafeAreaProvider,
  initialWindowMetrics,
} from 'react-native-safe-area-context';
import {Main} from './Main';

export const App = () => {
  return (
    <SafeAreaProvider initialMetrics={initialWindowMetrics}>
      <StatusBar
        barStyle={'dark-content'}
        backgroundColor={'#F4F4F4'}
        translucent={true}
      />
      <Main />
    </SafeAreaProvider>
  );
};
