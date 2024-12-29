const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    specPattern: "cypress/_test/e2e/**/*.cy.js",
    supportFile: false,
    defaultCommandTimeout: 10000,
    retries: {
      runMode: 1,
      openMode: 0,
    },
    video: false,
    screenshots: false,
    baseUrl: `http://localhost:${process.env.PORT || 3000}`, // Dynamic port
    reporter: "mochawesome",
    reporterOptions: {
      reportDir: "cypress/results",
      overwrite: true,
      html: false,
      json: true,
    },
  },
});
