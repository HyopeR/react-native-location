import {Converter, ParameterType, ReflectionKind} from 'typedoc';

/**
 * @param {import('typedoc').Application} app
 */
export function load(app) {
  const PLUGIN_NAME = '[typedoc-front-matter]';
  let config = {};

  app.options.addDeclaration({
    name: 'projectDetails',
    help: 'Provides dynamic front-matter support for documents.',
    type: ParameterType.Object,
    defaultValue: {},
  });

  app.converter.on(Converter.EVENT_BEGIN, () => {
    config = app.options.getValue('projectDetails');
    const count = Object.keys(config).length;
    app.logger.info(`${PLUGIN_NAME} ${count} settings detected.`);
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
    app.logger.verbose(`${PLUGIN_NAME} ${fileKey} updated.`);
  });
}
