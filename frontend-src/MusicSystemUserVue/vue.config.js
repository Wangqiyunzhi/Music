const path = require("path");

const backendStatic = path.resolve(
  __dirname,
  "../MusicSystemApi - idea/src/main/resources/static/user-ui"
);

module.exports = {
  publicPath: process.env.NODE_ENV === "production" ? "/user-ui/" : "/",
  outputDir: backendStatic,
  productionSourceMap: false,
  parallel: false,
  configureWebpack: {
    optimization: {
      minimize: false,
      splitChunks: false
    }
  },
  css: {
    extract: false
  },
  devServer: {
    port: 9173
  }
};
