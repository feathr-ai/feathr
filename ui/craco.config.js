const { loaderByName } = require("@craco/craco");
const CracoLessPlugin = require("craco-less");
const path = require("path");

const resolve = (dir) => path.resolve(__dirname, dir);

module.exports = {
  babel: {
    plugins: [
      [
        "import",
        {
          libraryName: "antd",
          libraryDirectory: "es",
          style: true,
        },
      ],
    ],
  },
  webpack: {
    alias: {
      "@": resolve("src"),
    },
  },
  plugins: [
    {
      plugin: CracoLessPlugin,
      options: {
        lessLoaderOptions: {
          lessOptions: {
            javascriptEnabled: true,
          },
        },
        // modifyLessModuleRule(lessModuleRule, context) {
        //   // Configure the file suffix
        //   lessModuleRule.test = /\.module\.less$/;

        //   // Configure the generated local ident name.
        //   const cssLoader = lessModuleRule.use.find(loaderByName("css-loader"));
        //   cssLoader.options.modules = {
        //     localIdentName: "[local]_[hash:base64:5]",
        //   };

        //   return lessModuleRule;
        // },
      },
    },
  ],
};
