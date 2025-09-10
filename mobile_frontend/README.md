# mobile_frontend - Q&A Agent Android App

A Kotlin Android app that serves as the frontend for a Q&A agent.

Features:
- Dark theme UI (primary #1976D2, secondary #424242)
- Top app bar with title
- Centered input field for user questions
- Scrollable conversation history with user/assistant bubbles
- REST client with mock fallback when backend URL is not configured

Build:
- Requires JDK 17 and Android SDK (compileSdk 34, minSdk 30)
- From the `mobile_frontend` directory:
  - Build: `./gradlew build`
  - Install debug: `./gradlew :app:installDebug`

Configure Backend:
- By default, the app uses a mock response (no network required).
- To connect to a backend, set the resource string `backend_base_url`:
  - Edit `app/src/main/res/values/config.xml` and set:
    `<string name="backend_base_url">https://your-backend.example.com</string>`
  - Expected endpoint: `POST {backend_base_url}/qa/ask`
    - Example request body: `{"question":"...","history":[{"role":"user","message":"..."},{"role":"assistant","message":"..."}]}`
    - Example response body: `{"answer":"..."}`
- INTERNET permission is declared in the manifest.

Notes:
- The app uses only traditional Android Views, not Jetpack Compose.
- Public interfaces are documented with comments and "PUBLIC_INTERFACE" markers where applicable.