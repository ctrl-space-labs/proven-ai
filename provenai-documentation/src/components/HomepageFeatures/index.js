import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

const FeatureList = [
  {
    title: 'Focus on What Matters',
    Svg: require('@site/static/img/undraw_docusaurus_tree.svg').default,
    description: (
      <>
        With ProvenAI, Educator can safely share their content with AI Agents and fill confident that they will be recognized.
          So, you can focus on your content!
      </>
    ),
  },
  {
      title: 'Easy to Use',
      Svg: require('@site/static/img/undraw_docusaurus_mountain.svg').default,
      description: (
          <>
              ProvenAI was designed from the ground up to be easily installed and
              used.
          </>
      ),
  },
  {
    title: 'Powered by Self Sovereign Identity',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        ProvenAI is built on top of Self Sovereign Identity, which allows you to own your data and control your identity.
          Verify the authenticity of your content and be recognized for your work.
      </>
    ),
  },
];

function Feature({Svg, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
