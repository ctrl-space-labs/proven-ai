/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */

// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
  // Define the sidebar structure
  tutorialSidebar: [
    // Automatically generate the sidebar items from the folder structure
    {
      type: 'autogenerated',
      dirName: '.', // Generates based on the folder structure
    },

    // Add the "Full API Reference" link manually at the end or desired position
    {
      type: 'link',
      label: 'Full API Reference',
      href: 'https://dev.proven-ai.ctrlspace.dev/proven-ai/api/v1/api-documentation', // Direct link to your full API reference
    },
  ],
};

export default sidebars;
