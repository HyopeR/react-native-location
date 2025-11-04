import React, {useCallback} from 'react';
import {
  SectionList,
  SectionListData,
  SectionListProps,
  SectionListRenderItem,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import {Card} from './commons/Card';
import {PageItem, PageSection} from './pages';

export type ListProps = {
  set: React.Dispatch<React.SetStateAction<string>>;
} & SectionListProps<PageItem, PageSection>;

type ListRender = SectionListRenderItem<PageItem, PageSection>;
type ListRenderSection = (info: {
  section: SectionListData<PageItem, PageSection>;
}) => React.ReactElement;

export const List = ({set, sections, ...props}: ListProps) => {
  const renderSectionHeader = useCallback<ListRenderSection>(({section}) => {
    const {title} = section;
    return (
      <View style={styles.section}>
        <Text style={styles.sectionText}>{title}</Text>
      </View>
    );
  }, []);

  const renderItem = useCallback<ListRender>(
    ({item, index}) => {
      const {title} = item;
      const text = `${index + 1}- ${title}`;
      return (
        <Card
          title={text}
          titleProps={{style: styles.itemText}}
          onPress={() => set(item.name)}
          style={styles.item}
        />
      );
    },
    [set],
  );

  const keyExtractor = useCallback((item: PageItem, index: number) => {
    return `example-${item.section}-${index}`;
  }, []);

  return (
    <SectionList
      sections={sections}
      stickySectionHeadersEnabled={false}
      renderSectionHeader={renderSectionHeader}
      SectionSeparatorComponent={() => <View style={styles.separator} />}
      renderItem={renderItem}
      ItemSeparatorComponent={() => <View style={styles.separator} />}
      keyExtractor={keyExtractor}
      contentContainerStyle={styles.container}
      {...props}
    />
  );
};

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: 8,
  },
  section: {
    flex: 1,
    height: 40,
    backgroundColor: '#FAFAFA',
    justifyContent: 'center',
    paddingHorizontal: 8,
    marginTop: 8,
  },
  sectionText: {
    textTransform: 'capitalize',
    fontSize: 14,
    fontWeight: '600',
  },
  item: {
    flex: 1,
    paddingHorizontal: 8,
  },
  itemText: {
    color: '#666',
    fontWeight: '400',
    fontSize: 14,
  },
  separator: {
    height: 4,
  },
});
