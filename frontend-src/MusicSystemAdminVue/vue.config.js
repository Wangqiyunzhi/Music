const path = require("path");

const backendStatic = path.resolve(
  __dirname,
  "../MusicSystemApi - idea/src/main/resources/static/admin-ui"
);

module.exports = {
  publicPath: process.env.NODE_ENV === "production" ? "/admin-ui/" : "/",
  outputDir: backendStatic,
  devServer: {
    port: 9172,
    proxy: {
      "/springboote2a78": {
        target: "http://127.0.0.1:9171",
        changeOrigin: true,
        ws: false,
        pathRewrite: {
          "^/springboote2a78": ""
        }
      }
    }
  }
};
