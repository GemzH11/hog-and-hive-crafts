import { defineConfig } from "vitest/config";

export default defineConfig({
  test: {
    environment: "jsdom",
    setupFiles: ["./src/test/setupTests.ts"],
    globals: true,
    coverage: {
      provider: "v8",
      reporter: ["text", "html", "lcov"],
      reportsDirectory: "./coverage",
      // optional: avoid counting types + test helper files
      exclude: ["**/mocks/*", "**/*.test.*", "**/*.unit.test.*", "src/test/**"],
    },
  },
});
