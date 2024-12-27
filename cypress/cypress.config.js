const { defineConfig } = require("cypress");
//import {defineConfig} from "cypress"
module.exports = defineConfig({
  e2e: {
    specPattern: "cypress/_test/e2e/**/*.cy.js", // Path to your test files
    supportFile: false, // Disable support file if not needed
    defaultCommandTimeout: 10000, // Timeout for commands
    retries: {
      runMode: 1, // Retry failed tests twice in `cypress run`
      openMode: 0, // No retries during interactive mode
    },
    video: true, // Enable video recording for debugging
  },
  env: {
    environment: "staging", // Custom environment variable
  },
});
