# Test Notes

- Some tests use Mockito for mocking Context and Resources (`org.mockito:mockito-core`).
- If CI does not include Mockito by default, add to `app` testing dependencies:
  `testImplementation("org.mockito:mockito-core:5.12.0")`
- These tests avoid instrumentation; they validate pure Kotlin logic paths.
- UI rendering test does not inflate Android XML (requires instrumentation). It asserts adapter logic (counts, types, and data binding assumptions).
