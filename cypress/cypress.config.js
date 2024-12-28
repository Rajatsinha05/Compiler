const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    specPattern: "cypress/_test/e2e/**/*.cy.js", // Path to your test files
    supportFile: false, // Disable support file if not needed
    defaultCommandTimeout: 10000, // Timeout for commands
    retries: {
      runMode: 1, // Retry failed tests once in `cypress run`
      openMode: 0, // No retries during interactive mode
    },
    video: false, // Disable video recording for debugging
    screenshot: false, // Disable screenshot
  },
  env: {
    environment: "staging", // Custom environment variable
  },
  reporter: "mochawesome", // Use mochawesome as the reporter
  reporterOptions: {
    reportDir: "cypress/results", // Directory to save reports
    overwrite: true, // Do not overwrite existing reports
    html: false, // Disable HTML report
    json: true // Enable JSON report
  }
});
