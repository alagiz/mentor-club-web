const cucumber = require("cypress-cucumber-preprocessor").default;
const browserify = require("@cypress/browserify-preprocessor");

const tsifyPlugin = ["tsify", { project: "../../tsconfig.json" }];
const options = browserify.defaultOptions;

options.browserifyOptions.plugin.unshift(tsifyPlugin);

module.exports = on => {
  on("file:preprocessor", cucumber(options));
};
