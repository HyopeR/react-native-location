import {Screen as ScreenBase, ScreenProps as ScreenBaseProps} from './Screen';
import {Header as HeaderBase, HeaderProps as HeaderBaseProps} from './Header';
import {
  Content as ContentBase,
  ContentProps as ContentBaseProps,
} from './Content';
import {Title as TitleBase, TitleProps as TitleBaseProps} from './Title';
import {
  Subtitle as SubtitleBase,
  SubtitleProps as SubtitleBaseProps,
} from './Subtitle';

export namespace ScreenNs {
  export type ScreenProps = ScreenBaseProps;
  export type HeaderProps = HeaderBaseProps;
  export type ContentProps = ContentBaseProps;
  export type TitleProps = TitleBaseProps;
  export type SubtitleProps = SubtitleBaseProps;
}

export const Screen = Object.assign(ScreenBase, {
  Header: HeaderBase,
  Content: ContentBase,
  Title: TitleBase,
  Subtitle: SubtitleBase,
});
