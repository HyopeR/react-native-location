import {Converter, ParameterType, ReflectionKind} from 'typedoc';

/**
 * @param {import('typedoc').Application} app
 */
export function load(app) {
  const PLUGIN_NAME = '[typedoc-front-matter]';
  let config = {};

  app.options.addDeclaration({
    name: 'projectDetails',
    help: 'FrontMatter configuration to be added to Markdown files.',
    type: ParameterType.Object,
    defaultValue: {},
  });

  app.converter.on(Converter.EVENT_BEGIN, () => {
    config = app.options.getValue('projectDetails');
    app.logger.info(`${PLUGIN_NAME} ${Object.keys(config).length} added.`);
  });

  app.converter.on(Converter.EVENT_RESOLVE, (context, reflection) => {
    if (reflection.kind !== ReflectionKind.Document) return;

    const fileName = reflection.name;
    const fileFrontMatter = reflection?.frontmatter || {};
    const fileKey = Object.keys(config).find(key => fileName.includes(key));
    if (!fileKey) return;

    const {title, ...omit} = config[fileKey];
    reflection.name = title || fileName;
    reflection.frontmatter = {...fileFrontMatter, ...omit};
  });
};
