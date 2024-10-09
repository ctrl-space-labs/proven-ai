// @ts-check
// `@type` JSDoc annotations allow editor autocompletion and type checking
// (when paired with `@ts-check`).
// There are various equivalent ways to declare your Docusaurus config.
// See: https://docusaurus.io/docs/api/docusaurus-config

import {themes as prismThemes} from 'prism-react-renderer';

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'ProvenAI',
  tagline: 'Govern, Trace, and Control AI Knowledge',
  favicon: 'img/Proven-Transp-1-fav-icon.png',

  // Set the production url of your site here
  url: 'https://ctrl-space-labs.github.io',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: '/proven-ai/',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'ctrl-space-labs', // Usually your GitHub org/user name.
  projectName: 'proven-ai', // Usually your repo name.

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: './sidebars.js',
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          editUrl:
            'https://github.com/ctrl-space-labs/proven-ai/tree/main/provenai-documentation',
        },
        blog: {
          showReadingTime: true,
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          editUrl:
            'https://github.com/ctrl-space-labs/proven-ai/tree/main/provenai-documentation',
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      // Replace with your project's social card
      image: 'img/docusaurus-social-card.jpg',
      navbar: {
        title: '',
        logo: {
          alt: 'ProvenAI Logo',
          src: 'img/Proven-Transp-1-crop.png',
        },
        items: [
          {
            type: 'docSidebar',
            sidebarId: 'tutorialSidebar',
            position: 'left',
            label: 'Tutorial',
          },
          {to: '/blog', label: 'Blog', position: 'left'},
          {
            href: 'https://github.com/ctrl-space-labs/proven-ai',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Docs',
            items: [
              {
                label: 'Tutorial',
                to: '/docs/Getting Started/Installation',
              },
            ],
          },
          {
            title: 'Community',
            items: [

              {
                label: 'Ctrl+Space Labs',
                href: 'https://www.ctrlspace.dev/',
              },
              {
                label: 'Discord',
                href: 'https://discord.com/invite/jWes2urauW',
              },
              {
                label: 'Instagram',
                href: 'https://www.instagram.com/ctrlspace.dev/',
              }
            ],
          },
          {
            title: 'More',
            items: [
              {
                label: 'Blog',
                to: '/blog',
              },
              {
                label: 'GitHub',
                href: 'https://github.com/ctrl-space-labs/proven-ai',
              },
            ],
          },
        ],
        copyright: `Copyright © ${new Date().getFullYear()} Ctrl+Space Labs.`,
      },
      prism: {
        additionalLanguages: ['java'],
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
      },
    }),
};

export default config;
