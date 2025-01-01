import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

const config: Config = {
  plugins: [require.resolve('docusaurus-lunr-search')],

  title: 'Improved Factions Docs',
  tagline: '🚀 Boost your Minecraft server with Improved Factions! 🌟',
  favicon: 'img/favicon.ico',

  url: 'https://toberocat.github.io',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: '/ImprovedFactions/',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'ToberoCat', // Usually your GitHub org/user name.
  projectName: 'ImprovedFactions', // Usually your repo name.

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
      {
        docs: {
          sidebarPath: './sidebars.ts',
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          editUrl:
            'https://github.com/ToberoCat/ImprovedFactions/tree/dev/improved-factions-docs',
        },
        gtag: {
          trackingID: 'GTM-WS4FNRJW',
          anonymizeIP: true,
        },
        // blog: {
        //   showReadingTime: true,
        //   feedOptions: {
        //     type: ['rss', 'atom'],
        //     xslt: true,
        //   },
        //   // Please change this to your repo.
        //   // Remove this to remove the "edit this page" links.
        //   editUrl:
        //     'https://github.com/facebook/docusaurus/tree/main/packages/create-docusaurus/templates/shared/',
        //   // Useful options to enforce blogging best practices
        //   onInlineTags: 'warn',
        //   onInlineAuthors: 'warn',
        //   onUntruncatedBlogPosts: 'warn',
        // },
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
    // Replace with your project's social card
    image: 'img/improved-factions-social-card.jpg',
    navbar: {
      title: 'Improved Factions',
      logo: {
        alt: 'Improved Factions Logo',
        src: 'img/logo.png',
      },
      items: [
        {
          to: 'docs/getting-started',
          label: 'Getting Started',
          position: 'left',
        },
        {
          to: 'docs/commands/base/create',
          label: 'Commands',
          position: 'left',
        },
        {
          type: 'dropdown',
          label: 'Modules',
          position: 'left',
          items: [
            {
              to: 'docs/Modules/power-raids',
              label: 'Power & Raids',
            },
          ],
        },
        {
          type: 'dropdown',
          label: 'Admin Resources',
          position: 'left',
          items: [
            {
              to: 'docs/permissions',
              label: 'Permissions',
            },
            {
              to: 'docs/placeholders',
              label: 'Papi Placeholders',
            },
          ],
        },
        {
          type: 'dropdown',
          label: 'Community',
          position: 'left',
          items: [

            {
              label: 'Github',
              href: 'https://github.com/ToberoCat/ImprovedFactions',
            },
            {
              label: 'Discord',
              href: 'https://discord.com/invite/VmSbFNZejz',
            },
            {
              label: 'AI Assisant',
              href: 'https://chatgpt.com/g/g-tvLQ5jbIz-improved-factions-assistant'
            },
            {
              label: 'Plugin Modrinth Page',
              href: 'https://modrinth.com/plugin/improved-factions',
            },
            {
              label: 'Plugin Spigot Page',
              href: 'https://www.spigotmc.org/resources/improved-factions.95617'
            }
          ],
        },
        {
          to: 'docs/qa',
          label: 'Q&A',
          position: 'left',
        },
        {
          href: 'https://github.com/ToberoCat/ImprovedFactions',
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
              label: 'Getting Started',
              to: '/docs/getting-started',
            },
            {
              label: 'Commands',
              to: '/docs/commands',
            },
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'Discord',
              href: 'https://discord.com/invite/VmSbFNZejz',
            },
            {
              label: 'Github',
              href: 'https://github.com/ToberoCat/ImprovedFactions',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/ToberoCat/ImprovedFactions',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Tobias Madlberger. Built with Docusaurus.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
